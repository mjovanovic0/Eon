package hr.eon.core.utils;

public final class SecurityUtils {

    private SecurityUtils() {}

    public static String crc( byte[] buffer ) {
        int result = 0;
        for ( final byte b : buffer ) {
            result = ( result << 1 ) ^ b;
        }
        return Integer.toString( result );
    }
}
