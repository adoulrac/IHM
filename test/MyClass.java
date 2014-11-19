package IHM.test;

/**
 * Created by greyna on 19/11/2014.
 */
public class MyClass {
    public int multiply(int a, int b) {
        return a * b;
    }

    public float divide(float a, float b) throws ArithmeticException {
        if (b==0.0) throw new ArithmeticException("/ by zero");
        return a / b;
    }
}
