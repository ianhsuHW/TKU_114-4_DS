abstract class Transport {
    private String routeName;

    Transport(String routeName) {
        this.routeName = routeName;
    }

    String getRouteName() {
        return routeName;
    }

    abstract int calculateFare(int distance);

    abstract String describe();
}

class Bus extends Transport {
    private int baseFare;

    Bus(String routeName, int baseFare) {
        super(routeName);
        this.baseFare = Math.max(0, baseFare);
    }

    @Override
    int calculateFare(int distance) {
        int km = Math.max(0, distance);
        if (km <= 5) {
            return baseFare;
        }
        return baseFare + (km - 5) * 3;
    }

    @Override
    String describe() {
        return "公車 " + getRouteName() + "（5 公里內單一票價 " + baseFare + "，超過每公里加 3）";
    }
}

class Taxi extends Transport {
    private int flagFare;
    private int perKm;

    Taxi(String routeName, int flagFare, int perKm) {
        super(routeName);
        this.flagFare = Math.max(0, flagFare);
        this.perKm = Math.max(0, perKm);
    }

    @Override
    int calculateFare(int distance) {
        int km = Math.max(0, distance);
        if (km <= 1) {
            return flagFare;
        }
        return flagFare + (km - 1) * perKm;
    }

    @Override
    String describe() {
        return "計程車 " + getRouteName() + "（起跳 " + flagFare + "，超過 1 公里每公里 " + perKm + "）";
    }
}

public class TransportFareSystem {
    public static void main(String[] args) {
        Transport[] transports = {
            new Bus("紅 32", 15),
            new Bus("藍 5", 20),
            new Taxi("市區隨招", 85, 25),
            new Taxi("機場接送", 120, 30)
        };

        int[] distances = { 3, 8, 12, 20 };

        System.out.println("=== 各路線票價（透過 overridden method）===");
        for (Transport transport : transports) {
            System.out.println(transport.describe());
            for (int distance : distances) {
                System.out.println("  " + distance + " 公里：" + transport.calculateFare(distance) + " 元");
            }
        }

        System.out.println();
        System.out.println("=== 同一段距離的比較（10 公里）===");
        Transport cheapest = transports[0];
        for (Transport transport : transports) {
            System.out.println(transport.getRouteName() + "：" + transport.calculateFare(10) + " 元");
            if (transport.calculateFare(10) < cheapest.calculateFare(10)) {
                cheapest = transport;
            }
        }
        System.out.println("最便宜：" + cheapest.getRouteName());

        System.out.println();
        System.out.println("=== 邊界值 ===");
        for (Transport transport : transports) {
            System.out.println(transport.getRouteName() + " 距離 0：" + transport.calculateFare(0)
                    + "，距離 -5：" + transport.calculateFare(-5));
        }

        System.out.println();
        System.out.println("main() 全程只呼叫 calculateFare()，沒有使用 instanceof 判斷型態後自行計價。");
    }
}
