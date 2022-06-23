package org.github.palace.bot.asm;

import lombok.Data;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * A parser to make a {@link ClassVisitor} visit a ClassFile structure.
 * <p>
 * 借鉴 @see org.springframework.asm
 *
 * @author jihongyuan
 * @date 2022/6/4 21:31
 */
@Data
public class ClassReader {

    /**
     * Constant pool info
     */
    private ConstantInfo[] cpInfos;

    private int[] cpInfoOffsets;

    /**
     * 原始字节码
     */
    private byte[] classBuff;

    /**
     * 辅助解析
     */
    @Getter
    private ClassByteBuffer classBuffer;

    public ClassReader(final byte[] classBytes) {
        this.classBuff = classBytes;
        this.classBuffer = new ClassByteBuffer(classBytes);

        classBuffer.getU8();

        // Constant pool count
        int cpCount = readUnsignedShort(classBuffer.getU2());

        cpInfos = new ConstantInfo[cpCount];

        for (int currentCpInfoIndex = 1; currentCpInfoIndex < cpCount; currentCpInfoIndex++) {
            // tag
            int tag = classBuffer.getU1()[0] & 0xFF;
            ConstantInfo cpInfo = null;
            switch (tag) {
                case CpTag.CONSTANT_UTF8_TAG:
                    int len = readUnsignedShort(classBuffer.getU2());
                    cpInfo = new Utf8Info(len, new String(classBuffer.get(len)));
                    break;
                case CpTag.CONSTANT_INTEGER_TAG:
                    cpInfo = new IntegerInfo(readInt(classBuffer.getU4()));
                    break;
                case CpTag.CONSTANT_FLOAT_TAG:
                    cpInfo = new FloatInfo(Float.intBitsToFloat(readInt(classBuffer.getU4())));
                    break;
                case CpTag.CONSTANT_LONG_TAG:
                    cpInfo = new LongInfo(readLong(classBuffer.getU8()));
                    break;
                case CpTag.CONSTANT_DOUBLE_TAG:
                    cpInfo = new DoubleInfo(Double.longBitsToDouble(readLong(classBuffer.getU8())));
                    break;
                case CpTag.CONSTANT_CLASS_TAG:
                    cpInfo = new ClassInfo(readUnsignedShort(classBuffer.getU2()), this::readCpInfoUtf8);
                    break;
                case CpTag.CONSTANT_STRING_TAG:
                    cpInfo = new StringInfo(readUnsignedShort(classBuffer.getU2()), this::readCpInfoUtf8);
                    break;
                case CpTag.CONSTANT_FIELDREF_TAG:
                    cpInfo = new FieldrefInfo(readUnsignedShort(classBuffer.getU2()), readUnsignedShort(classBuffer.getU2()));
                    break;
                case CpTag.CONSTANT_METHODREF_TAG:
                    cpInfo = new MethodrefInfo(readUnsignedShort(classBuffer.getU2()), readUnsignedShort(classBuffer.getU2()));
                    break;
                case CpTag.CONSTANT_INTERFACE_METHODREF_TAG:
                    cpInfo = new InterfaceMethodrefInfo(readUnsignedShort(classBuffer.getU2()), readUnsignedShort(classBuffer.getU2()));
                    break;
                case CpTag.CONSTANT_NAME_AND_TYPE_TAG:
                    cpInfo = new NameAndTypeInfo(readCpInfoUtf8(readUnsignedShort(classBuffer.getU2())), readCpInfoUtf8(readUnsignedShort(classBuffer.getU2())));
                    break;
                case CpTag.CONSTANT_METHOD_HANDLE_TAG:
                    cpInfo = new MethodHandleInfo(readUnsignedShort(classBuffer.getU1()), readUnsignedShort(classBuffer.getU2()));
                    break;
                case CpTag.CONSTANT_METHOD_TYPE_TAG:
                    cpInfo = new MethodTypeInfo(readUnsignedShort(classBuffer.getU2()));
                    break;
                case CpTag.CONSTANT_DYNAMIC_TAG:
                    cpInfo = new DynamicInfo(readUnsignedShort(classBuffer.getU2()), readUnsignedShort(classBuffer.getU2()));
                    break;
                case CpTag.CONSTANT_INVOKE_DYNAMIC_TAG:
                    cpInfo = new InvokeDynamicInfo(readUnsignedShort(classBuffer.getU2()), readUnsignedShort(classBuffer.getU2()));
                    break;
                case CpTag.CONSTANT_MODULE_TAG:
                    cpInfo = new ModuleInfo(readUnsignedShort(classBuffer.getU2()));
                    break;
                case CpTag.CONSTANT_PACKAGE_TAG:
                    cpInfo = new PackageInfo(readUnsignedShort(classBuffer.getU2()));
                    break;
                default:
                    System.out.println(tag);
            }
            cpInfos[currentCpInfoIndex] = cpInfo;

            // long 和 double 占两个常量池格子
            // {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.4.5}
            if (tag == CpTag.CONSTANT_LONG_TAG || tag == CpTag.CONSTANT_DOUBLE_TAG) {
                currentCpInfoIndex += 1;
            }
        }

        for (ConstantInfo cpInfo : cpInfos) {
            if (cpInfo != null) {
                cpInfo.end();
            }
        }

    }

    public void accept(final ClassVisitor classVisitor) {
        int accessFlag = readUnsignedShort(classBuffer.getU2());
        String thisClass = readCpInfoClass(readUnsignedShort(classBuffer.getU2()));
        String superClass = readCpInfoClass(readUnsignedShort(classBuffer.getU2()));

        String[] interfaces = new String[readUnsignedShort(classBuffer.getU2())];
        for (int i = 0; i < interfaces.length; i++) {
            interfaces[i] = readCpInfoClass(readUnsignedShort(classBuffer.getU2()));
        }

        FieldInfo[] fieldInfos = new FieldInfo[readUnsignedShort(classBuffer.getU2())];
        for (int i = 0; i < fieldInfos.length; i++) {
            int fieldAccessFlag = readUnsignedShort(classBuffer.getU2());
            String fieldName = readCpInfoUtf8(readUnsignedShort(classBuffer.getU2()));
            String fieldDescriptor = readCpInfoUtf8(readUnsignedShort(classBuffer.getU2()));
            fieldInfos[i] = new FieldInfo(fieldAccessFlag, fieldName, fieldDescriptor);

            int fieldAttrCount = readUnsignedShort(classBuffer.getU2());
            while (fieldAttrCount-- > 0) {
                // attr_name_index
                skip(2);
                classBuffer.getU2();
                int attrLen = readUnsignedShort(classBuffer.getU4());
                skip(attrLen);
            }
        }

        MethodInfo[] methodInfos = new MethodInfo[readUnsignedShort(classBuffer.getU2())];
        for (int i = 0; i < methodInfos.length; i++) {
            int methodAccessFlag = readUnsignedShort(classBuffer.getU2());
            String methodName = readCpInfoUtf8(readUnsignedShort(classBuffer.getU2()));
            String methodDescriptor = readCpInfoUtf8(readUnsignedShort(classBuffer.getU2()));
            methodInfos[i] = new MethodInfo(methodAccessFlag, methodName, methodDescriptor);

            int methodAttrCount = readUnsignedShort(classBuffer.getU2());
            while (methodAttrCount-- > 0) {
                // attr_name_index
                skip(2);
                int attrLen = readInt(classBuffer.getU4());
                skip(attrLen);
            }
        }


        // visit
        classVisitor.visit(accessFlag, thisClass, superClass, interfaces);

        for (FieldInfo fieldInfo : fieldInfos) {
            classVisitor.visitField(fieldInfo);
        }

        for (MethodInfo methodInfo : methodInfos) {
            classVisitor.visitMethod(methodInfo);
        }

        int attributesCount = readUnsignedShort(classBuffer.getU2());
        for (int i = 0; i < attributesCount; i++) {
            String descriptor = readCpInfoUtf8(readUnsignedShort(classBuffer.getU2()));
            int attrLen = readInt(classBuffer.getU4());

            // {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.7.10}
            if (AttributesInfo.SOURCE_FILE.equals(descriptor)) {
                classVisitor.visitSourceFile(readCpInfoUtf8(readUnsignedShort(classBuffer.getU2())));
            }
            else if (AttributesInfo.RUNTIME_VISIBLE_ANNOTATIONS.equals(descriptor)) {
                // attrLength
                int numAnnotations = readUnsignedShort(classBuffer.getU2());
                while (numAnnotations-- > 0) {
                    String annotationDescriptor = readCpInfoUtf8(readUnsignedShort(classBuffer.getU2()));
                    readElementValues(classVisitor.visitVisibleAnnotation(annotationDescriptor), true);
                }
            } else {
                skip(attrLen);
            }
        }

        classVisitor.visitEnd();
    }

    /**
     * @param named 区分  和 annotation_value 和 array_value
     */
    private void readElementValues(final AnnotationVisitor annotationVisitor, final boolean named) {
        int numElementValuePairs = readUnsignedShort(classBuffer.getU2());

        while (numElementValuePairs-- > 0) {
            if (named) {
                // annotation_value
                String elementName = readCpInfoUtf8(readUnsignedShort(classBuffer.getU2()));
                readElementValue(annotationVisitor, elementName);
            } else {
                // array_value
                readElementValue(annotationVisitor, null);
            }
        }

        if (annotationVisitor != null) {
            annotationVisitor.visitEnd();
        }
    }

    private void readElementValue(final AnnotationVisitor annotationVisitor, String elementName) {

        int tag = classBuffer.getU1()[0] & 0xFF;
        // {@link https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html#jvms-4.2.1
        switch (tag & 0xFF) {
            case 'B': // const_value_index, byte
                annotationVisitor.visit(
                        elementName,
                        (byte) readCpInfoInt(readUnsignedShort(classBuffer.getU2()))
                );
                break;
            case 'C': // const_value_index, char
                annotationVisitor.visit(
                        elementName,
                        (char) readCpInfoInt(readUnsignedShort(classBuffer.getU2()))
                );
                break;
            case 'D': // const_value_index, double
                annotationVisitor.visit(
                        elementName,
                        readCpInfoDouble(readUnsignedShort(classBuffer.getU2()))
                );
                break;
            case 'F': // const_value_index, float
                annotationVisitor.visit(
                        elementName,
                        readCpInfoFloat(readUnsignedShort((classBuffer.getU2())))
                );
                break;
            case 'I': // const_value_index, int
                annotationVisitor.visit(
                        elementName,
                        readCpInfoInt(readUnsignedShort(classBuffer.getU2()))
                );
                break;
            case 'J': // const_value_index, long
                annotationVisitor.visit(
                        elementName,
                        readCpInfoLong(readUnsignedShort(classBuffer.getU2()))
                );
                break;
            case 'S': // const_value_index, short
                annotationVisitor.visit(
                        elementName,
                        (short) readCpInfoInt(readUnsignedShort(classBuffer.getU2()))
                );
                break;
            case 'Z': // const_value_index, boolean
                annotationVisitor.visit(
                        elementName,
                        readCpInfoInt(readUnsignedShort(classBuffer.getU2())) == 0
                                ? Boolean.FALSE
                                : Boolean.TRUE);
                break;
            case 's': // const_value_index, string
                annotationVisitor.visit(
                        elementName,
                        readCpInfoUtf8(readUnsignedShort(classBuffer.getU2())));
                break;
            case 'e':// enum_const_value, enum
                annotationVisitor.visitEnum(
                        elementName,
                        // type_name_index
                        readCpInfoUtf8(readUnsignedShort(classBuffer.getU2())),
                        // const_name_index
                        readCpInfoUtf8(readUnsignedShort(classBuffer.getU2())));
                break;
            case 'c': // class_info_index, class
                annotationVisitor.visit(
                        elementName,
                        readCpInfoUtf8(readUnsignedShort(classBuffer.getU2())));
                break;
            case '@': // annotation_value, annotation
                readElementValues(
                        annotationVisitor.visitAnnotation(
                                elementName,
                                readCpInfoUtf8(readUnsignedShort(classBuffer.getU2()))),
                        true);
                break;
            case '[': // array_value, array
                // TODO num_values
                readElementValues(annotationVisitor.visitArray(elementName), false);
                break;
            default:
        }
    }

    public void skip(int offset) {
        classBuffer.get(offset);
    }

    /**
     * Read cp info.
     */
    public String readCpInfoClass(int index) {
        return ((ClassInfo) cpInfos[index]).getClassName();
    }

    /**
     * Read cp info.
     */
    public double readCpInfoDouble(int index) {
        return ((DoubleInfo) cpInfos[index]).getValue();
    }

    /**
     * Read cp info.
     */
    public double readCpInfoFloat(int index) {
        return ((FloatInfo) cpInfos[index]).getBytes();
    }

    /**
     * Read cp info.
     */
    public String readCpInfoUtf8(int index) {
        return ((Utf8Info) cpInfos[index]).getBytes();
    }


    /**
     * Read cp info.
     */
    public int readCpInfoInt(int index) {
        return ((IntegerInfo) cpInfos[index]).getBytes();
    }

    /**
     * Read cp info.
     */
    public long readCpInfoLong(int index) {
        return ((LongInfo) cpInfos[index]).getValue();
    }

    /**
     * Read 2 bytes as unsigned short.
     */
    public int readUnsignedShort(byte[] bytes) {
        return ((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF);
    }

    /**
     * Read 4 bytes as int
     */
    public int readInt(byte[] classBuffer) {
        return ((classBuffer[0] & 0xFF) << 24) | ((classBuffer[1] & 0xFF) << 16) | ((classBuffer[2] & 0xFF) << 8) | (classBuffer[3] & 0xFF);
    }

    /**
     * Read 8 bytes as long
     */
    public long readLong(byte[] bytes) {
        long l1 = readInt(new byte[]{bytes[0], bytes[1], bytes[2], bytes[3]});
        long l0 = readInt(new byte[]{bytes[4], bytes[5], bytes[6], bytes[7]}) & 0xFFFFFFFFL;
        return (l1 << 32) | l0;
    }

    public static byte[] readStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }
}
