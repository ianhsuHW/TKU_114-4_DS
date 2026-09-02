// 課堂實作題五：社群 Adjacency List
// 需求：支援使用者、好友關係、共同好友、解除好友及查詢孤立使用者。

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocialNetworkGraph {
    // LinkedHashMap + LinkedHashSet：輸出順序穩定，方便對照結果
    private final Map<String, Set<String>> friends = new LinkedHashMap<>();

    public boolean addUser(String user) {
        if (user == null || user.isBlank()) return false;
        return friends.putIfAbsent(user.trim(), new LinkedHashSet<>()) == null;
    }

    // undirected：兩邊的 Set 都要更新，否則會出現單向好友
    public boolean addFriendship(String first, String second) {
        if (!known(first) || !known(second) || first.equals(second)) return false;
        boolean changed = friends.get(first).add(second);
        friends.get(second).add(first);
        return changed;
    }

    public boolean unfriend(String first, String second) {
        if (!known(first) || !known(second)) return false;
        boolean changed = friends.get(first).remove(second);
        friends.get(second).remove(first);
        return changed;
    }

    public List<String> friendsOf(String user) {
        Set<String> set = friends.get(user);
        return set == null ? List.of() : new ArrayList<>(set);
    }

    public List<String> mutualFriends(String first, String second) {
        if (!known(first) || !known(second)) return List.of();
        Set<String> mutual = new LinkedHashSet<>(friends.get(first));
        mutual.retainAll(friends.get(second));
        return new ArrayList<>(mutual);
    }

    public boolean areFriends(String first, String second) {
        return known(first) && friends.get(first).contains(second);
    }

    // 孤立使用者：已註冊但一個好友都沒有
    public List<String> isolatedUsers() {
        List<String> isolated = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : friends.entrySet()) {
            if (entry.getValue().isEmpty()) isolated.add(entry.getKey());
        }
        return isolated;
    }

    public int userCount() {
        return friends.size();
    }

    // undirected edge 在兩個 Set 各出現一次，所以 degree 總和要除以 2
    public int friendshipCount() {
        int degreeSum = 0;
        for (Set<String> set : friends.values()) degreeSum += set.size();
        return degreeSum / 2;
    }

    private boolean known(String user) {
        return user != null && friends.containsKey(user);
    }

    public void printNetwork() {
        for (Map.Entry<String, Set<String>> entry : friends.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        SocialNetworkGraph network = new SocialNetworkGraph();
        for (String user : List.of("Amy", "Ben", "Cara", "Dan", "Eve", "Fay")) {
            network.addUser(user);
        }
        System.out.println("addUser(Amy) again=" + network.addUser("Amy"));
        System.out.println("addUser(\"  \")=" + network.addUser("  "));

        network.addFriendship("Amy", "Ben");
        network.addFriendship("Amy", "Cara");
        network.addFriendship("Ben", "Cara");
        network.addFriendship("Ben", "Dan");
        network.addFriendship("Cara", "Dan");
        network.addFriendship("Eve", "Amy");

        System.out.println("duplicate friendship=" + network.addFriendship("Amy", "Ben"));
        System.out.println("self friendship=" + network.addFriendship("Amy", "Amy"));
        System.out.println("unknown user=" + network.addFriendship("Amy", "Zoe"));

        System.out.println();
        System.out.println("[network]");
        network.printNetwork();
        System.out.println("users=" + network.userCount()
                + " friendships=" + network.friendshipCount());

        System.out.println();
        System.out.println("[query]");
        System.out.println("Amy friends=" + network.friendsOf("Amy"));
        System.out.println("Amy & Ben mutual=" + network.mutualFriends("Amy", "Ben"));
        System.out.println("Amy & Dan mutual=" + network.mutualFriends("Amy", "Dan"));
        System.out.println("Eve & Ben mutual=" + network.mutualFriends("Eve", "Ben"));
        System.out.println("areFriends(Amy,Dan)=" + network.areFriends("Amy", "Dan"));
        System.out.println("isolated=" + network.isolatedUsers());

        System.out.println();
        System.out.println("[unfriend]");
        System.out.println("unfriend(Amy,Ben)=" + network.unfriend("Amy", "Ben"));
        System.out.println("unfriend(Amy,Ben) again=" + network.unfriend("Amy", "Ben"));
        System.out.println("unfriend(Eve,Amy)=" + network.unfriend("Eve", "Amy"));
        System.out.println("Amy friends=" + network.friendsOf("Amy"));
        System.out.println("Ben friends=" + network.friendsOf("Ben") + " (雙向同步移除)");
        System.out.println("isolated=" + network.isolatedUsers());
        System.out.println("friendships=" + network.friendshipCount());

        System.out.println();
        System.out.println("missing user friends=" + network.friendsOf("Zoe"));
    }
}
