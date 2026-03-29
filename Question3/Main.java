import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Request {
    String name;
    String room;
    int priority;

    Request(String name, String room, int priority) {
        this.name = name;
        this.room = room;
        this.priority = priority;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) {
            sc.close();
            return;
        } // Keamanan dasar
        int n = sc.nextInt();

        // 1. Membaca Ruangan yang tersedia
        for (int i = 0; i < n; i++) {
            sc.next();
        }

        // 2. Membaca Nama dan Ruangan yang dipesan
        String[] names = new String[n];
        String[] rooms = new String[n];
        for (int i = 0; i < n; i++) {
            names[i] = sc.next();
            rooms[i] = sc.next();
        }

        // 3. Membaca Prioritas dan Memasukkan ke Queue
        Queue<Request> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            int priority = sc.nextInt();
            queue.add(new Request(names[i], rooms[i], priority));
        }
        sc.close();

        // --- FASE EKSTRAKSI DAN SORTING ---
        while (!queue.isEmpty()) {
            // Intip ruangan apa yang diminta oleh orang paling depan
            String targetRoom = queue.peek().room;

            // Tempat penampungan sementara untuk ruangan yang sama
            ArrayList<Request> group = new ArrayList<>();

            // Ekstraksi antrean: Ambil yang ruangannya sama, sisanya suruh antre ulang di
            // belakang
            int currentSize = queue.size();
            for (int i = 0; i < currentSize; i++) {
                Request req = queue.poll();
                if (req.room.equals(targetRoom)) {
                    group.add(req);
                } else {
                    queue.add(req);
                }
            }

            // Insertion Sort untuk ArrayList 'group' berdasarkan 'priority'
            for (int j = 1; j < group.size(); j++) {
                Request key = group.get(j);
                int k = j - 1;

                while (k >= 0 && group.get(k).priority > key.priority) {
                    group.set(k + 1, group.get(k));
                    k = k - 1;
                }
                group.set(k + 1, key);
            }

            // Cetak hasil yang sudah di-sort
            for (int i = 0; i < group.size(); i++) {
                System.out.println(group.get(i).name + " | " + group.get(i).room);
            }
        }
    }
}