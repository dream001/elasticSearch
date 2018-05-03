package util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Generator {

    public static final int OID_AMOUNT = 1000;
    public static final String OID_BASE_INITIAL_VAL = "10000000000000000000";
    public static final String GROUP_PK_CORP = "0001";
    private static String INTERN_FIXER = "AA";
    private static Map oidMap = new ConcurrentHashMap();

    private String lock = "lock";
    private volatile String nextOid = null;
    
    private Map<String, Lock> locks = new HashMap();

    public String[] generate(String pk_corp, int amount) {
        if (pk_corp == null) {
            throw new IllegalArgumentException("Can't generate primary key with null pk_corp");
        }
        String[] newOids = new String[amount];
        for (int i = 0; i < amount; ++i) {
            newOids[i] = nextOid("kudu", pk_corp);
            System.out.println("newOids[i]" + newOids[i]);
        }
        return newOids;
    }

    public synchronized String nextOid(String dataSource, String groupNumber) {
        String ds = dataSource;
        String oidBase = null;
        String oidMark = INTERN_FIXER;
        Lock l = getLock(ds, groupNumber);
        l.lock();
        try {
            String key = groupNumber + ds;
            OidCounter oidCounter = (OidCounter) oidMap.get(key);
            if (oidCounter == null) {
                oidCounter = new OidCounter();
                oidMap.put(key, oidCounter);
            }

            oidBase = oidCounter.oidBase;
            nextOid = GeneratorItem.getInstance(oidBase).nextOidBase();
            oidCounter.oidBase = nextOid;
            oidCounter.amount += 1;
        } finally {
            l.unlock();
        }
        return getWholeOid(groupNumber, oidMark, nextOid);
    }


    private Lock getLock(String ds, String groupNumber) {
        String lockKey = "" + ds + ":" + groupNumber;
        Lock l = (Lock) this.locks.get(lockKey);
        if (l == null) {
            synchronized (this.lock) {
                l = (Lock) this.locks.get(lockKey);
                if (l == null) {
                    l = new ReentrantLock();
                    this.locks.put(lockKey, l);
                }
            }
        }
        return l;
    }

    private final String getWholeOid(String groupNumber, String oidMark, String oidBase) {
        return groupNumber + oidMark + oidBase;
    }

    private class OidCounter {
        public String oidBase;
        public int amount;

        public OidCounter() {
            this.amount = 0;
            this.oidBase = "10000000000000";
        }
    }


    public static void main(String[] args) {

        Generator ge = new Generator();
        ge.generate("0001F1100000002IGYSD", 4);
    }


    public String getKey() {
        String str = ClusterUtils.getHostPK4() + "_" + System.currentTimeMillis();
        String key = null;
        if (str.length() < 20) {
            key = getZero(19 - str.length()) + "_" + str;
        } else if (str.length() > 20) {
            key = str.substring(0, 19);
        } else {
            key = str;
        }
        return key;
    }

    public String getZero(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("0");
        }
        return sb.toString();
    }
}
