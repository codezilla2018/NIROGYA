package codezilla.foss.nirogya;
public class SensorFilter {

    private SensorFilter() {
    }
    public static float sum(float[] array) {
        float returnValue = 0;
        for (int i = 0; i < array.length; i++) {
            returnValue += array[i];
        }
        return returnValue;
    }
    public static float norm(float[] array) {
        float returnValue = 0;
        for (int i = 0; i < array.length; i++) {
            returnValue += array[i] * array[i];
        }
        return (float) Math.sqrt(returnValue);
    }
    public static float dot(float[] a, float[] b) {
        float returnValue = a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
        return returnValue;
    }

}