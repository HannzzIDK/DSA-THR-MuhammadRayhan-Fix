import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

// Class Card murni, tidak ada embel-embel "implements"
class Card {
    int val, cat;

    Card(int val, int cat) {
        this.val = val;
        this.cat = cat;
    }

    @Override
    public String toString() {
        return val + "," + cat;
    }
}

public class Main {

    // Kita buat method sorting manual (Bubble Sort) untuk menggantikan
    // Collections.sort()
    static void sortHand(ArrayList<Card> hand) {
        int n = hand.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Card c1 = hand.get(j);
                Card c2 = hand.get(j + 1);

                // Aturan tukar: Jika kategori lebih besar, ATAU (kategori sama tapi nilai lebih
                // besar)
                if (c1.cat > c2.cat || (c1.cat == c2.cat && c1.val > c2.val)) {
                    // Lakukan pertukaran (Swap)
                    hand.set(j, c2);
                    hand.set(j + 1, c1);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        ArrayList<ArrayList<Card>> players = new ArrayList<>();

        // Membaca input kartu untuk 4 pemain
        for (int i = 0; i < 4; i++) {
            ArrayList<Card> hand = new ArrayList<>();
            String line = sc.nextLine().trim();
            String[] tokens = line.split(" ");

            for (String t : tokens) {
                String[] parts = t.split(",");
                hand.add(new Card(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
            }

            // Gunakan sorting manual kita di sini
            sortHand(hand);
            players.add(hand);
        }

        int currentTurn = sc.nextInt() - 1;
        sc.close();

        Stack<Card> stack = new Stack<>();
        Card lastPlayedCard = null;
        int lastPlayerToPlay = -1;
        int winner = -1;

        // --- SIMULASI GAME BERAKSI ---
        while (true) {
            // Skenario 1: Buka Jalan (Ronde Baru)
            if (lastPlayedCard == null || currentTurn == lastPlayerToPlay) {
                Card toPlay = players.get(currentTurn).remove(0); // Ambil kartu terkecil
                stack.push(toPlay);
                lastPlayedCard = toPlay;
                lastPlayerToPlay = currentTurn;
            }
            // Skenario 2: Melawan Tumpukan Atas
            else {
                ArrayList<Card> hand = players.get(currentTurn);
                Card toPlay = null;

                // Cari kartu pertama yang kategorinya sama dan nilainya lebih tinggi
                for (int i = 0; i < hand.size(); i++) {
                    Card c = hand.get(i);
                    if (c.cat == lastPlayedCard.cat && c.val > lastPlayedCard.val) {
                        toPlay = c;
                        hand.remove(i);
                        break;
                    }
                }

                if (toPlay != null) {
                    stack.push(toPlay);
                    lastPlayedCard = toPlay;
                    lastPlayerToPlay = currentTurn;
                }
            }

            // Cek Kemenangan
            if (players.get(currentTurn).isEmpty()) {
                winner = currentTurn + 1;
                break;
            }

            // Giliran Lanjut
            currentTurn = (currentTurn + 1) % 4;
        }

        // --- CETAK HASIL ---
        System.out.println(winner);

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}