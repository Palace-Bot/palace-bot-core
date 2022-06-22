package org.github.palace.bot.asm;

import lombok.Data;
import lombok.Getter;

/**
 * @author jihongyuan
 * @date 2022/6/6 14:31
 */
@Data
public class ConstantInfo {
    protected int tag;

    public ConstantInfo(int tag) {
        this.tag = tag;
    }
}

@Getter
class Utf8Info extends ConstantInfo {

    // u2
    private final Integer length;

    private final String bytes;

    public Utf8Info(Integer length, String bytes) {
        super(CpTag.CONSTANT_UTF8_TAG);
        this.length = length;
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "Utf8Info{" +
                "length=" + length +
                ", bytes='" + bytes + '\'' +
                '}';
    }
}

@Getter
class IntegerInfo extends ConstantInfo {
    // u4
    private final Integer bytes;

    public IntegerInfo(Integer bytes) {
        super(CpTag.CONSTANT_INTEGER_TAG);
        this.bytes = bytes;
    }
}

@Getter
class FloatInfo extends ConstantInfo {
    // u4
    private final float bytes;

    public FloatInfo(float bytes) {
        super(CpTag.CONSTANT_FLOAT_TAG);
        this.bytes = bytes;
    }
}

@Getter
class LongInfo extends ConstantInfo {
    // u8
    private final long value;

    public LongInfo(long value) {
        super(CpTag.CONSTANT_LONG_TAG);
        this.value = value;
    }
}

@Getter
class DoubleInfo extends ConstantInfo {
    // u8
    private final double value;

    public DoubleInfo(double value) {
        super(CpTag.CONSTANT_DOUBLE_TAG);
        this.value = value;
    }
}

@Getter
class ClassInfo extends ConstantInfo {
    // 2
    private final Integer index;

    public ClassInfo(Integer index) {
        super(CpTag.CONSTANT_CLASS_TAG);
        this.index = index;
    }
}

@Getter
class StringInfo extends ConstantInfo {
    // 2
    private final Integer index;

    public StringInfo(Integer index) {
        super(CpTag.CONSTANT_STRING_TAG);
        this.index = index;
    }
}

@Getter
class FieldrefInfo extends ConstantInfo {
    // 2
    private final Integer index1;

    // 2
    private final Integer index2;

    public FieldrefInfo(Integer index1, Integer index2) {
        super(CpTag.CONSTANT_FIELDREF_TAG);
        this.index1 = index1;
        this.index2 = index2;
    }
}

@Getter
class MethodrefInfo extends ConstantInfo {
    // 2
    private final Integer index1;

    // 2
    private final Integer index2;

    public MethodrefInfo(Integer index1, Integer index2) {
        super(CpTag.CONSTANT_METHODREF_TAG);
        this.index1 = index1;
        this.index2 = index2;
    }
}

@Getter
class InterfaceMethodrefInfo extends ConstantInfo {
    // 2
    private final Integer index1;

    // 2
    private final Integer index2;

    public InterfaceMethodrefInfo(Integer index1, Integer index2) {
        super(CpTag.CONSTANT_INTERFACE_METHODREF_TAG);
        this.index1 = index1;
        this.index2 = index2;
    }
}

@Getter
class NameAndTypeInfo extends ConstantInfo {
    private final String name;

    private final String descriptor;

    public NameAndTypeInfo(String name, String descriptor) {
        super(CpTag.CONSTANT_NAME_AND_TYPE_TAG);
        this.name = name;
        this.descriptor = descriptor;
    }
}

@Getter
class MethodHandleInfo extends ConstantInfo {
    // 1
    private final Integer referenceKind;

    // 2
    private final Integer referenceIndex;

    public MethodHandleInfo(Integer referenceKind, Integer referenceIndex) {
        super(CpTag.CONSTANT_METHOD_HANDLE_TAG);
        this.referenceKind = referenceKind;
        this.referenceIndex = referenceIndex;
    }
}

@Getter
class MethodTypeInfo extends ConstantInfo {
    // 2
    private final Integer descriptorIndex;

    public MethodTypeInfo(Integer descriptorIndex) {
        super(CpTag.CONSTANT_METHOD_TYPE_TAG);
        this.descriptorIndex = descriptorIndex;
    }
}

@Getter
class DynamicInfo extends ConstantInfo {
    // 2
    private final Integer bootstrapMethodAttrIndex;
    private final Integer nameAndTypeIndex;

    public DynamicInfo(Integer bootstrapMethodAttrIndex, Integer nameAndTypeIndex) {
        super(CpTag.CONSTANT_DYNAMIC_TAG);
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }
}

@Getter
class InvokeDynamicInfo extends ConstantInfo {
    // 2
    private final Integer bootstrapMethodAttrIndex;
    private final Integer nameAndTypeIndex;

    public InvokeDynamicInfo(Integer bootstrapMethodAttrIndex, Integer nameAndTypeIndex) {
        super(CpTag.CONSTANT_INVOKE_DYNAMIC_TAG);
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }
}

@Getter
class ModuleInfo extends ConstantInfo {
    // 2
    private final Integer nameIndex;

    public ModuleInfo(Integer nameIndex) {
        super(CpTag.CONSTANT_MODULE_TAG);
        this.nameIndex = nameIndex;
    }
}

@Getter
class PackageInfo extends ConstantInfo {
    // 2
    private final Integer nameIndex;

    public PackageInfo(Integer nameIndex) {
        super(CpTag.CONSTANT_PACKAGE_TAG);
        this.nameIndex = nameIndex;
    }
}