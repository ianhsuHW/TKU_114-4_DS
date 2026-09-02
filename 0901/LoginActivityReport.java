// 課後作業二：登入紀錄分析
// 需求：使用 HashMap 統計每個帳號次數，HashSet 找出不同 IP 數量，
//       輸出異常重複登入報告。

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivityReport {

    record LoginRecord(String account, String ip) {
        LoginRecord {
            if (account == null || account.isBlank()) {
                throw new IllegalArgumentException("account");
            }
            if (ip == null || ip.isBlank()) throw new IllegalArgumentException("ip");
            account = account.trim().toLowerCase();
            ip = ip.trim();
        }
    }

    private final Map<String, Integer> loginCounts = new HashMap<>();
    private final Map<String, Set<String>> ipsPerAccount = new HashMap<>();

    public void record(LoginRecord login) {
        // merge：key 不存在時放 1，已存在時累加
        loginCounts.merge(login.account(), 1, Integer::sum);
        // HashSet 自動去除重複 IP，size 就是不同 IP 數
        ipsPerAccount.computeIfAbsent(login.account(), key -> new HashSet<>())
                .add(login.ip());
    }

    public int loginCount(String account) {
        return loginCounts.getOrDefault(account, 0);
    }

    public int distinctIpCount(String account) {
        return ipsPerAccount.getOrDefault(account, Set.of()).size();
    }

    public Set<String> allIps() {
        Set<String> all = new HashSet<>();
        for (Set<String> ips : ipsPerAccount.values()) all.addAll(ips);
        return all;
    }

    // 異常：登入次數過多，或同一帳號來自太多不同 IP
    public List<String> abnormalReport(int countThreshold, int ipThreshold) {
        List<String> report = new ArrayList<>();
        for (String account : sortedAccounts()) {
            int count = loginCount(account);
            int ips = distinctIpCount(account);
            List<String> reasons = new ArrayList<>();
            if (count >= countThreshold) reasons.add("logins=" + count);
            if (ips >= ipThreshold) reasons.add("distinctIps=" + ips);
            if (!reasons.isEmpty()) {
                report.add(account + " " + reasons + " ips=" + sortedIps(account));
            }
        }
        return report;
    }

    // HashMap 沒有固定順序，報告前先取出 key 排序
    private List<String> sortedAccounts() {
        List<String> accounts = new ArrayList<>(loginCounts.keySet());
        accounts.sort(Comparator.naturalOrder());
        return accounts;
    }

    private List<String> sortedIps(String account) {
        List<String> ips = new ArrayList<>(ipsPerAccount.getOrDefault(account, Set.of()));
        ips.sort(Comparator.naturalOrder());
        return ips;
    }

    public void printSummary() {
        System.out.println("account   | logins | distinctIps | ips");
        for (String account : sortedAccounts()) {
            System.out.printf("%-9s | %6d | %11d | %s%n",
                    account, loginCount(account), distinctIpCount(account),
                    sortedIps(account));
        }
    }

    public static void main(String[] args) {
        LoginActivityReport report = new LoginActivityReport();

        String[][] raw = {
                {"amy", "10.0.0.1"}, {"ben", "10.0.0.2"}, {"AMY", "10.0.0.1"},
                {"amy", "10.0.0.9"}, {"cara", "192.168.1.5"}, {"ben", "10.0.0.2"},
                {"amy", "203.0.113.7"}, {"amy", "10.0.0.1"}, {"dan", "172.16.0.3"},
                {"amy", "198.51.100.4"}, {"ben", "10.0.0.8"}, {" Amy ", "10.0.0.1"}};
        for (String[] pair : raw) {
            report.record(new LoginRecord(pair[0], pair[1]));
        }

        System.out.println("[summary]");
        report.printSummary();

        System.out.println();
        System.out.println("total records=" + raw.length);
        System.out.println("distinct accounts=" + report.loginCounts.size());
        System.out.println("distinct ips overall=" + report.allIps().size());

        System.out.println();
        System.out.println("[abnormal: logins>=5 or distinctIps>=3]");
        for (String line : report.abnormalReport(5, 3)) {
            System.out.println("  " + line);
        }

        System.out.println();
        System.out.println("[single account]");
        System.out.println("amy logins=" + report.loginCount("amy")
                + " distinctIps=" + report.distinctIpCount("amy"));
        System.out.println("unknown logins=" + report.loginCount("zoe")
                + " distinctIps=" + report.distinctIpCount("zoe"));

        System.out.println();
        try {
            report.record(new LoginRecord("  ", "10.0.0.1"));
        } catch (IllegalArgumentException e) {
            System.out.println("blank account -> IllegalArgumentException: "
                    + e.getMessage());
        }
    }
}
