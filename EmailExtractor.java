import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class EmailExtractor {
    public static void main(String[] args) 
    {
        String csvFile = "input/Kontakter.CSV"; // Replace with your CSV file path
        String outputFile = "output/result.txt"; // Replace with the desired output file path

        // Extract email addresses from the CSV file and write them to the output file
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            String line;
            Set<String> uniqueEmails = new HashSet<>();

            // Read the CSV file line by line
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");

                // Extract email addresses from each column
                for (String column : columns) 
                {
                    // Split by semicolon to extract multiple email addresses from the same column
                    String[] addresses = column.split(";");

                    // Add the extracted email addresses to the set of unique email addresses
                    for (String address : addresses) {
                        String trimmedAddress = address.trim().replace("\"","").toLowerCase();
                        if (isValidEmail(trimmedAddress)) 
                        {
                            uniqueEmails.add(trimmedAddress);
                        }
                    }
                }
            }

            // Write the unique email addresses to the output file
            for (String email : uniqueEmails) {
                bw.write(email);
                bw.newLine();
            }

            System.out.println("Email addresses extracted successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static boolean isValidEmail(String email) 
    {
        var regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        // Check if the email is valid
        if(!email.matches(regexPattern))
            return false;

        // Check if the email contains a blacklisted word from the file "blacklist.txt"
        try (BufferedReader br = new BufferedReader(new FileReader("input/blacklist.txt"))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (email.contains(line)) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
