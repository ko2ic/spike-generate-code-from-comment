/* Header Comment */
package sample;

import java.util.ArrayList;
import java.util.List;

import com.google.common.annotations.Beta;
import com.google.common.eventbus.Subscribe;

// <root bind="ParserTarget" replace="A1">
/**
 * class doc<br>
 * @author ko2ic
 */
@Beta
public class ParserTarget {

    /**
     * enum doc<br>
     * @author ko2ic
     */
    private enum EnumType {
        Type_A, Type_B, Type_C
        // <delete>
        , Ttpe_DELETE
        // </delete>
    };

    /** String doc */
    private static final String A_STR = "a";

    /** list doc */
    protected static List<EnumType> list = new ArrayList<>();

    // comment2

    /**
     * main method doc<br>
     * @param args
     */
    @Subscribe
    public static void main(String[] args) {
        // comment3
        ParserTarget target = new ParserTarget();
        // <delete>
        target.method1("method", 1);
        // </delete>
        System.out.println(target.method2());
        target.method3(EnumType.Type_B);

        // <items bind="EnumType.Type_A" replace="EnumType.Type_B">
        list.add(EnumType.Type_A);
        // </items>

        // <items bind="a" replace="A3">
        System.out.println("A2=" + "a");
        // </items>

    }

    // <delete>
    /**
     * method1 doc<br>
     * @param str string
     * @param one integer
     */
    void method1(String str, int one) {
        System.out.println(str + one);
    }

    // </delete>

    /**
     * method2 doc<br>
     * @return a str
     */
    protected String method2() {
        return A_STR;
    }

    /**
     * method3 doc<br>
     * @param type B
     */
    private void method3(EnumType type) {
        System.out.println(type.name());
    }

    // </root>

    // finish
}