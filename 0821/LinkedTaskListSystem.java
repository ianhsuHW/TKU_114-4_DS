class Task {
    private String id;
    private String title;

    Task(String id, String title) {
        this.id = id;
        this.title = title;
    }

    String getId() {
        return id;
    }

    @Override
    public String toString() {
        return id + " " + title;
    }
}

class TaskNode {
    Task task;
    TaskNode next;

    TaskNode(Task task) {
        this.task = task;
    }
}

class TaskLinkedList {
    private TaskNode head;
    private int size;

    boolean addFirst(Task task) {
        if (!canAdd(task)) {
            return false;
        }
        TaskNode node = new TaskNode(task);
        node.next = head;
        head = node;
        size++;
        return true;
    }

    boolean addLast(Task task) {
        if (!canAdd(task)) {
            return false;
        }
        TaskNode node = new TaskNode(task);
        if (head == null) {
            head = node;
        } else {
            TaskNode current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = node;
        }
        size++;
        return true;
    }

    boolean insertAfter(String existingId, Task task) {
        if (!canAdd(task)) {
            return false;
        }
        TaskNode target = nodeOf(existingId);
        if (target == null) {
            System.out.println("插入失敗，找不到 id：" + existingId);
            return false;
        }
        TaskNode node = new TaskNode(task);
        node.next = target.next;
        target.next = node;
        size++;
        return true;
    }

    Task findById(String id) {
        TaskNode node = nodeOf(id);
        return node == null ? null : node.task;
    }

    boolean removeById(String id) {
        if (head == null) {
            System.out.println("刪除失敗，清單為空：" + id);
            return false;
        }
        if (head.task.getId().equals(id)) {
            head = head.next;
            size--;
            return true;
        }
        TaskNode previous = head;
        while (previous.next != null) {
            if (previous.next.task.getId().equals(id)) {
                previous.next = previous.next.next;
                size--;
                return true;
            }
            previous = previous.next;
        }
        System.out.println("刪除失敗，找不到 id：" + id);
        return false;
    }

    int size() {
        return size;
    }

    void printAll() {
        StringBuilder sb = new StringBuilder("清單(" + size + ")：");
        if (head == null) {
            sb.append("(空)");
        }
        TaskNode current = head;
        while (current != null) {
            sb.append("[").append(current.task).append("]");
            if (current.next != null) {
                sb.append(" -> ");
            }
            current = current.next;
        }
        System.out.println(sb);
    }

    private TaskNode nodeOf(String id) {
        TaskNode current = head;
        while (current != null) {
            if (current.task.getId().equals(id)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private boolean canAdd(Task task) {
        if (task == null) {
            System.out.println("加入失敗：task 為 null");
            return false;
        }
        if (nodeOf(task.getId()) != null) {
            System.out.println("加入失敗，id 重複：" + task.getId());
            return false;
        }
        return true;
    }
}

public class LinkedTaskListSystem {
    public static void main(String[] args) {
        TaskLinkedList list = new TaskLinkedList();

        System.out.println("=== 空 list 測試 ===");
        list.printAll();
        System.out.println("findById(T01)：" + list.findById("T01"));
        System.out.println("removeById(T01)：" + list.removeById("T01"));
        System.out.println("size：" + list.size());

        System.out.println();
        System.out.println("=== 加入 ===");
        System.out.println("addLast T02：" + list.addLast(new Task("T02", "撰寫測試")));
        System.out.println("addLast T03：" + list.addLast(new Task("T03", "程式碼審查")));
        System.out.println("addFirst T01：" + list.addFirst(new Task("T01", "需求分析")));
        System.out.println("addLast T04：" + list.addLast(new Task("T04", "部署上線")));
        list.printAll();

        System.out.println();
        System.out.println("=== 重複 id ===");
        System.out.println("addLast T02：" + list.addLast(new Task("T02", "重複工作")));
        System.out.println("addFirst T01：" + list.addFirst(new Task("T01", "重複工作")));
        System.out.println("size：" + list.size());

        System.out.println();
        System.out.println("=== insertAfter ===");
        System.out.println("insertAfter T02 加入 T25：" + list.insertAfter("T02", new Task("T25", "整合測試")));
        System.out.println("insertAfter T99 加入 T26：" + list.insertAfter("T99", new Task("T26", "不存在")));
        list.printAll();

        System.out.println();
        System.out.println("=== findById ===");
        System.out.println("findById(T25)：" + list.findById("T25"));
        System.out.println("findById(T99)：" + list.findById("T99"));

        System.out.println();
        System.out.println("=== 刪除 head ===");
        System.out.println("removeById(T01)：" + list.removeById("T01"));
        list.printAll();

        System.out.println("=== 刪除 middle ===");
        System.out.println("removeById(T25)：" + list.removeById("T25"));
        list.printAll();

        System.out.println("=== 刪除 tail ===");
        System.out.println("removeById(T04)：" + list.removeById("T04"));
        list.printAll();

        System.out.println("=== 刪除不存在的 id ===");
        System.out.println("removeById(T99)：" + list.removeById("T99"));
        list.printAll();
        System.out.println("size：" + list.size());
    }
}
