package org.test.asm;

/**
 * @author jihongyuan
 * @date 2022/6/22 9:43
 */
@AsmAnnotation(
        byteValue = 10,
        charValue = 'a',
        doubleValue = 10d,
        floatValue = 11f,
        intValue = 12,
        longValue = 13L,
        shortValue = 14,
        booleanValue = true,
        stringValue = "15",
        enumValue = IEnum.TEST,
        classValue = Void.class,
        arrayValue = {
                @Mapping(str = "1"),
                @Mapping(str = "2"),
                @Mapping(str = "3"),
                @Mapping(str = "4"),
        },
        mapping = @Mapping(str = "5")
)
public class AsmClassTest {
}
