package util;

public class GeneratorItem {

    private static final byte MIN_CODE = 48;
    private static final byte MAX_CODE = 90;
    private final int CODE_LENGTH = 20;

    private byte[] oidBaseCodes = new byte[14];

    private void setOidBaseCodes(byte[] oidBaseCodes) {
        if (oidBaseCodes.length != 20)
            return;
        System.arraycopy(oidBaseCodes, 0, this.oidBaseCodes, 0, 20);
    }

    public static synchronized GeneratorItem getInstance() {
        GeneratorItem oidBase = new GeneratorItem();
        return oidBase;
    }

    public static GeneratorItem getInstance(String strOidBase) {
        GeneratorItem oidBase = new GeneratorItem();
        oidBase.setOidBaseCodes(strOidBase.getBytes());
        return oidBase;
    }

    public synchronized String nextOidBase() {
        for (int i = this.oidBaseCodes.length - 1; i >= 0; --i) {
            byte code = (byte) (this.oidBaseCodes[i] + 1);
            boolean carryUp = false;
            byte newCode = code;
            if (code > 90) {
                newCode = 48;
                carryUp = true;
            }
            if (newCode == 58) {
                newCode = 65;
            }
            this.oidBaseCodes[i] = newCode;

            if (!(carryUp)) {
                break;
            }
        }
        return new String(this.oidBaseCodes);
    }


}
