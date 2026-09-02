// 課後作業五：網站連結 Graph
// 需求：建立 directed adjacency list，輸出 outgoing links、incoming count、
//       無 incoming 頁面與無 outgoing 頁面。

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebsiteLinkGraph {
    // 只保存 outgoing edge；incoming 由掃描全部 outgoing 得到
    private final Map<String, Set<String>> outgoing = new LinkedHashMap<>();

    public boolean addPage(String page) {
        if (page == null || page.isBlank()) return false;
        return outgoing.putIfAbsent(page.trim(), new LinkedHashSet<>()) == null;
    }

    // directed edge：只有 from 的 Set 會改變，不能自動補上反向連結
    public boolean addLink(String from, String to) {
        if (!known(from) || !known(to) || from.equals(to)) return false;
        return outgoing.get(from).add(to);
    }

    public boolean removeLink(String from, String to) {
        if (!known(from) || !known(to)) return false;
        return outgoing.get(from).remove(to);
    }

    public List<String> outgoingLinks(String page) {
        Set<String> links = outgoing.get(page);
        return links == null ? List.of() : new ArrayList<>(links);
    }

    public List<String> incomingLinks(String page) {
        List<String> result = new ArrayList<>();
        if (!known(page)) return result;
        for (Map.Entry<String, Set<String>> entry : outgoing.entrySet()) {
            if (entry.getValue().contains(page)) result.add(entry.getKey());
        }
        return result;
    }

    // in-degree 要掃描其他頁面的 outgoing，不能拿自己的 outgoing size 當答案
    public int incomingCount(String page) {
        return incomingLinks(page).size();
    }

    public int outgoingCount(String page) {
        return known(page) ? outgoing.get(page).size() : 0;
    }

    // 沒有 incoming：外部進不來的孤島或首頁
    public List<String> pagesWithoutIncoming() {
        List<String> result = new ArrayList<>();
        for (String page : outgoing.keySet()) {
            if (incomingCount(page) == 0) result.add(page);
        }
        return result;
    }

    // 沒有 outgoing：使用者走進去就出不來的死路
    public List<String> pagesWithoutOutgoing() {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : outgoing.entrySet()) {
            if (entry.getValue().isEmpty()) result.add(entry.getKey());
        }
        return result;
    }

    public int linkCount() {
        int total = 0;
        for (Set<String> links : outgoing.values()) total += links.size();
        return total;
    }

    private boolean known(String page) {
        return page != null && outgoing.containsKey(page);
    }

    public void printReport() {
        System.out.println("page       | out | in | outgoing links");
        for (String page : outgoing.keySet()) {
            System.out.printf("%-10s | %3d | %2d | %s%n",
                    page, outgoingCount(page), incomingCount(page),
                    outgoingLinks(page));
        }
    }

    public static void main(String[] args) {
        WebsiteLinkGraph site = new WebsiteLinkGraph();
        for (String page : List.of("/index", "/about", "/products",
                "/products/a", "/contact", "/promo")) {
            site.addPage(page);
        }

        System.out.println("[add links]");
        System.out.println("/index -> /about       = " + site.addLink("/index", "/about"));
        System.out.println("/index -> /products    = " + site.addLink("/index", "/products"));
        System.out.println("/products -> /products/a = "
                + site.addLink("/products", "/products/a"));
        System.out.println("/about -> /contact     = " + site.addLink("/about", "/contact"));
        System.out.println("/products/a -> /contact= "
                + site.addLink("/products/a", "/contact"));
        System.out.println("/promo -> /index       = " + site.addLink("/promo", "/index"));
        System.out.println("duplicate link         = " + site.addLink("/index", "/about"));
        System.out.println("self link              = " + site.addLink("/index", "/index"));
        System.out.println("unknown page           = " + site.addLink("/index", "/blog"));
        System.out.println("linkCount=" + site.linkCount());

        System.out.println();
        site.printReport();

        System.out.println();
        System.out.println("[analysis]");
        System.out.println("/contact incoming=" + site.incomingLinks("/contact")
                + " count=" + site.incomingCount("/contact"));
        System.out.println("/index outgoing=" + site.outgoingLinks("/index"));
        System.out.println("/index incoming=" + site.incomingLinks("/index")
                + " (directed，不會自動反向)");
        System.out.println("no incoming (入口/孤島)=" + site.pagesWithoutIncoming());
        System.out.println("no outgoing (死路)=" + site.pagesWithoutOutgoing());

        System.out.println();
        System.out.println("[remove]");
        System.out.println("removeLink(/index,/about)=" + site.removeLink("/index", "/about"));
        System.out.println("removeLink again=" + site.removeLink("/index", "/about"));
        System.out.println("/about incoming count=" + site.incomingCount("/about"));
        System.out.println("no incoming=" + site.pagesWithoutIncoming());
        System.out.println("linkCount=" + site.linkCount());

        System.out.println();
        System.out.println("missing page outgoing=" + site.outgoingLinks("/blog"));
        System.out.println("missing page incoming=" + site.incomingCount("/blog"));
    }
}
