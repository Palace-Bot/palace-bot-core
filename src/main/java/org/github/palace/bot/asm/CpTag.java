package org.github.palace.bot.asm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jihongyuan
 * @date 2022/6/7 15:52
 */
public class CpTag {

    /** The tag value of CONSTANT_Class_info JVMS structures. */
    static final int CONSTANT_CLASS_TAG = 7;

    /** The tag value of CONSTANT_Fieldref_info JVMS structures. */
    static final int CONSTANT_FIELDREF_TAG = 9;

    /** The tag value of CONSTANT_Methodref_info JVMS structures. */
    static final int CONSTANT_METHODREF_TAG = 10;

    /** The tag value of CONSTANT_InterfaceMethodref_info JVMS structures. */
    static final int CONSTANT_INTERFACE_METHODREF_TAG = 11;

    /** The tag value of CONSTANT_String_info JVMS structures. */
    static final int CONSTANT_STRING_TAG = 8;

    /** The tag value of CONSTANT_Integer_info JVMS structures. */
    static final int CONSTANT_INTEGER_TAG = 3;

    /** The tag value of CONSTANT_Float_info JVMS structures. */
    static final int CONSTANT_FLOAT_TAG = 4;

    /** The tag value of CONSTANT_Long_info JVMS structures. */
    static final int CONSTANT_LONG_TAG = 5;

    /** The tag value of CONSTANT_Double_info JVMS structures. */
    static final int CONSTANT_DOUBLE_TAG = 6;

    /** The tag value of CONSTANT_NameAndType_info JVMS structures. */
    static final int CONSTANT_NAME_AND_TYPE_TAG = 12;

    /** The tag value of CONSTANT_Utf8_info JVMS structures. */
    static final int CONSTANT_UTF8_TAG = 1;

    /** The tag value of CONSTANT_MethodHandle_info JVMS structures. */
    static final int CONSTANT_METHOD_HANDLE_TAG = 15;

    /** The tag value of CONSTANT_MethodType_info JVMS structures. */
    static final int CONSTANT_METHOD_TYPE_TAG = 16;

    /** The tag value of CONSTANT_Dynamic_info JVMS structures. */
    static final int CONSTANT_DYNAMIC_TAG = 17;

    /** The tag value of CONSTANT_InvokeDynamic_info JVMS structures. */
    static final int CONSTANT_INVOKE_DYNAMIC_TAG = 18;

    /** The tag value of CONSTANT_Module_info JVMS structures. */
    static final int CONSTANT_MODULE_TAG = 19;

    /** The tag value of CONSTANT_Package_info JVMS structures. */
    static final int CONSTANT_PACKAGE_TAG = 20;

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class FieldInfo {
    private int access;
    private String name;
    private String descriptor;
    private Object[] attributes;

    public FieldInfo(int access, String name, String descriptor) {
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
    }

}

@Data
@NoArgsConstructor
@AllArgsConstructor
class MethodInfo {
    private int access;
    private String name;
    private String descriptor;
    private Object[] attributes;

    public MethodInfo(int access, String name, String descriptor) {
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
    }

}
