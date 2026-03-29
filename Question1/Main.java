import java.util.Scanner;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;

//pake bantuan chatgpt

public class Main {

    public static void sort(int[] aurors) {
        int n = aurors.length;
        for (int i = 0; i < n; i++) {
            int key = aurors[i];
            int j = i - 1;
            while (j >= 0 && aurors[j] > key) {
                aurors[j + 1] = aurors[j];
                j = j - 1;
            }
            aurors[j + 1] = key;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt())
            return;

        int n = sc.nextInt();
        int tLimit = sc.nextInt(); // Membaca batas waktu (T) dari soal

        int[] aurors = new int[n];
        for (int i = 0; i < n; i++) {
            aurors[i] = sc.nextInt();
        }
        sc.close();

        sort(aurors); // Urutkan dari yang tercepat ke terlambat

        Queue<String> log = new LinkedList<>();
        Stack<Integer> trapped = new Stack<>();

        // inLeft[i] = true artinya Auror tersebut masih ada di Hutan (Sisi Kiri)
        boolean[] inLeft = new boolean[n];
        for (int i = 0; i < n; i++) {
            inLeft[i] = true;
        }

        int totalTime = 0;
        int sisaOrang = n;
        boolean timeout = false;

        // --- FASE 1: SIKLUS 4 LANGKAH (STRATEGI GREEDY) ---
        while (sisaOrang > 3) {
            // Langkah 1: Dua tercepat nyebrang
            if (totalTime + aurors[1] > tLimit) {
                timeout = true;
                break;
            }
            totalTime += aurors[1];
            inLeft[0] = false;
            inLeft[1] = false;
            log.add(aurors[0] + " " + aurors[1] + " ->");

            // Langkah 2: Paling cepat balik bawa senter
            if (totalTime + aurors[0] > tLimit) {
                timeout = true;
                break;
            }
            totalTime += aurors[0];
            inLeft[0] = true;
            log.add(aurors[0] + " <-");

            // Langkah 3: Dua paling lambat nyebrang bersamaan
            if (totalTime + aurors[sisaOrang - 1] > tLimit) {
                timeout = true;
                break;
            }
            totalTime += aurors[sisaOrang - 1];
            inLeft[sisaOrang - 2] = false;
            inLeft[sisaOrang - 1] = false;
            log.add(aurors[sisaOrang - 2] + " " + aurors[sisaOrang - 1] + " ->");

            // Langkah 4: Tercepat kedua balik bawa senter
            if (totalTime + aurors[1] > tLimit) {
                timeout = true;
                break;
            }
            totalTime += aurors[1];
            inLeft[1] = true;
            log.add(aurors[1] + " <-");

            sisaOrang -= 2; // Dua orang paling lambat sudah selamat
        }

        // --- FASE 2: PENYELESAIAN SISA ORANG (JIKA WAKTU MASIH ADA) ---
        if (!timeout) {
            if (sisaOrang == 3) {
                if (totalTime + aurors[1] <= tLimit) {
                    totalTime += aurors[1];
                    inLeft[0] = false;
                    inLeft[1] = false;
                    log.add(aurors[0] + " " + aurors[1] + " ->");

                    if (totalTime + aurors[0] <= tLimit) {
                        totalTime += aurors[0];
                        inLeft[0] = true;
                        log.add(aurors[0] + " <-");

                        if (totalTime + aurors[2] <= tLimit) {
                            totalTime += aurors[2];
                            inLeft[0] = false;
                            inLeft[2] = false;
                            log.add(aurors[0] + " " + aurors[2] + " ->");
                        }
                    }
                }
            } else if (sisaOrang == 2) {
                if (totalTime + aurors[1] <= tLimit) {
                    totalTime += aurors[1];
                    inLeft[0] = false;
                    inLeft[1] = false;
                    log.add(aurors[0] + " " + aurors[1] + " ->");
                }
            } else if (sisaOrang == 1) {
                if (totalTime + aurors[0] <= tLimit) {
                    totalTime += aurors[0];
                    inLeft[0] = false;
                    log.add(aurors[0] + " ->");
                }
            }
        }

        // --- FASE 3: MENGHITUNG KORBAN (NON-SURVIVORS) ---
        // Kita push dari indeks terbesar ke terkecil agar saat di-pop keluarnya urut
        // dari kecil ke besar
        for (int i = n - 1; i >= 0; i--) {
            if (inLeft[i]) {
                trapped.push(aurors[i]);
            }
        }

        // --- FASE 4: CETAK OUTPUT ---
        while (!log.isEmpty()) {
            System.out.println(log.poll());
        }

        if (!trapped.isEmpty()) {
            System.out.print("Non-survivors: [");
            boolean first = true;
            while (!trapped.isEmpty()) {
                if (!first)
                    System.out.print(", ");
                System.out.print(trapped.pop());
                first = false;
            }
            System.out.println("]");
        }
    }
}