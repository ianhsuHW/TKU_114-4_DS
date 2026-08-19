abstract class Device { private String name; Device(String name){ this.name = name; } String getName(){ return name; } abstract void inspect(); }
class Phone extends Device { Phone(String name){ super(name); } @Override void inspect(){ System.out.println(getName() + " checked"); } }
class Tablet extends Device { Tablet(String name){ super(name); } @Override void inspect(){ System.out.println(getName() + " checked"); } }
public class DeviceInspectionSystem {
    public static void main(String[] args) { Device[] devices = { new Phone("P1"), new Tablet("T1") }; for (Device d : devices) d.inspect(); }
}
