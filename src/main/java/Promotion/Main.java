    package Promotion;

    import Promotion.service.InputParser;

    public class Main {
        public static void main(String[] args) {
            if (args.length < 2) {
                System.out.println("Usage: java Main <orders.json> <paymentmethods.json>");
                return;
            }
            String order_file;
            String payment_file;
            order_file = args[0];
            payment_file = args[1];
            String filename = "C:\\Users\\cypri\\IdeaProjects\\Cyprian_Kozubek_Java21_Wroclaw\\src\\main\\java\\Promotion\\orders.json";
            InputParser inputParser = new InputParser();
            inputParser.parseOrder(filename);
            filename = "C:\\Users\\cypri\\IdeaProjects\\Cyprian_Kozubek_Java21_Wroclaw\\src\\main\\java\\Promotion\\paymentmethods.json";
            inputParser.parsePaymentMethods(filename);
        }
    }
