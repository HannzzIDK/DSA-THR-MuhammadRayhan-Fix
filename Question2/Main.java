import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) {
            sc.close();
            return;
        } // Keamanan dasar
        int n = sc.nextInt();

        Stack<String> stack = new Stack<>();
        Queue<String> queue = new LinkedList<>();

        // Membaca semua token input yang tersisa
        int count = 0;
        while (sc.hasNext()) {
            String token = sc.next();
            if (count < n) {
                stack.push(token); // Masuk ke Stack (Grup 1)
            } else {
                queue.add(token); // Masuk ke Queue (Grup 2)
            }
            count++;
        }
        sc.close();

        // -- FASE 1: MENYUSUN URUTAN TOMBOL KALKULATOR (DIBALIK) --
        Stack<String> buildStack = new Stack<>();

        while (!stack.isEmpty() && !queue.isEmpty()) {
            String sVal = stack.pop();
            String qVal = queue.poll();

            // Masukkan Queue dulu, baru Stack agar saat di-pop nanti
            // keluar Stack duluan (Sesuai urutan S lalu Q)
            buildStack.push(qVal);
            buildStack.push(sVal);
        }

        // -- FASE 2: SIMULASI KALKULATOR FISIK --
        int result = 0;
        int currentNumber = 0;
        boolean hasCurrentNumber = false;
        String pendingOp = "+"; // Default awal agar angka pertama ditambahkan ke 0

        while (!buildStack.isEmpty()) {
            String token = buildStack.pop();

            if (isNumber(token)) {
                if (hasCurrentNumber) {
                    // Gabungkan digit jika angka ditekan berurutan (misal 1 lalu 7 jadi 17)
                    currentNumber = currentNumber * 10 + Integer.parseInt(token);
                } else {
                    currentNumber = Integer.parseInt(token);
                    hasCurrentNumber = true;
                }
            } else {
                // Jika token adalah operator (+, -, *, /)
                if (hasCurrentNumber) {
                    // Hitung operasi sebelumnya yang tertunda
                    result = applyOp(result, pendingOp, currentNumber);
                    hasCurrentNumber = false;
                }
                // Simpan operator baru (akan menimpa operator lama jika diketik berurutan)
                pendingOp = token;
            }
        }

        // Eksekusi angka terakhir di layar yang belum dihitung
        if (hasCurrentNumber) {
            result = applyOp(result, pendingOp, currentNumber);
        }

        // Cetak hasil akhir
        System.out.println(result);
    }

    // Method pembantu untuk mengecek apakah token adalah angka
    static boolean isNumber(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Method pembantu untuk menghitung matematika dasar
    static int applyOp(int a, String op, int b) {
        switch (op) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return (b != 0) ? a / b : 0;
            default:
                return 0;
        }
    }
}