import java.io.*;
import java.util.Scanner;

import static java.lang.System.in;

public class CMD {
    /**
     * 1) "mkdir" cmd create file/folder +
     * 2) "echo" write to file +
     * 3) "copy" copy file +
     * 4) "read" read from file +
     * 5) "rmdir" delete folder/file +
     * 6) "rename" file/folder +
     * 7) "dir" +
     * 8) "cd" +
     */
    static StringBuilder currentPath = new StringBuilder("C:/");
    static Scanner sc = new Scanner(in);

    public static void main(String[] args) {
        System.out.println("Welcome! ");
        String cmd = "";

        while (!cmd.equals("quit") && !cmd.equals("exit")) {
            System.out.print(currentPath.toString() + " : ");
            cmd = sc.nextLine();
            switch (cmd) {
                case "dir" -> {
                    dir();
                }
                default -> {
                    if (cmd.startsWith("cd")) {
                        String subFolder = cmd.split(" ")[1];

                        if (subFolder.equals("..")) {
                            int last = currentPath.lastIndexOf("/");
                            currentPath = new StringBuilder(currentPath.substring(0, last));
                        } else {
                            gotoFolder(subFolder);
                        }
                    } else if (cmd.startsWith("mkdir")) {
                        mkdir(cmd);
                    } else if (cmd.startsWith("rmdir")) {
                        rmdir(cmd);
                    } else if (cmd.startsWith("copy")) {
                        String[] file = cmd.split(" ");
                        String in = file[1];
                        String out = file[2];
                        copy(in, out);
                    } else if (cmd.startsWith("rename")) {
                        String[] file = cmd.split(" ");
                        String in = file[1];
                        String out = file[2];
                        rename(in, out);
                    } else if (cmd.startsWith("read")) {
                        String file = cmd.split(" ")[1];
                        try {
                            read(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (cmd.startsWith("echo")) {
                        String file1 = cmd.split("/")[1];
                        String file2 = cmd.split("/")[2];
                        echo(file1, file2);
                    } else if (cmd.equals("help")) {
                        System.out.println("""
                                CMD commands:
                                1. dir - show directory
                                2. cd - mode directory
                                3. mkdir - make directory
                                4. rmdir - remove directory
                                5. copy ... ... - copy file from ... to ...
                                6. rename ... ... - rename file from ... to ...
                                7. read ... - read file
                                8. echo ... ... - echo file writer
                                9. quit or exit - exit program
                                10. help - cmd commands
                                """);
                    }
                }
            }
        }
        System.out.println("Good bye!");
    }

    private static void echo(String file, String file2) {
        try {
            FileWriter myWriter = new FileWriter(currentPath + "/" + file);
            myWriter.write(file2);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static void read(String inner) throws IOException {
        FileInputStream in;

        in = new FileInputStream(currentPath + "/" + inner);
        while (in.available() > 0) {
            System.out.print((char) in.read());
        }
        System.out.println();
        in.close();
    }

    private static void rename(String inner, String outer) {
        File oldName = new File(currentPath + "/" + inner);
        File newName = new File(currentPath + "/" + outer);

        if (oldName.renameTo(newName))
            System.out.println("Renamed successfully");
        else
            System.out.println("Error");
    }

    // rmdir - folder va file uchun ishladi. lekin exeption otyapti. tuzaldi shekilli:
    private static void rmdir(String cmd) {
        String eq = cmd.split(" ")[1];
        File file = new File(currentPath.toString());
        File[] files = file.listFiles();
        assert files != null;
        for (File f1 : files) {
            if (f1.getName().equals(eq)) {
                f1.delete();
                System.out.println("o`chirildi");
            }
        }
    }

    private static void mkdir(String cmd) {
        String eq = cmd.split(" ")[1];
        File file = new File(currentPath + "/" + eq);
        if (eq.endsWith("txt")) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            file.mkdir();
        }

    }

    private static void dir() {
        File file = new File(currentPath.toString());
        String[] files = file.list();

        assert files != null;
        for (String s : files) {
            System.out.println(s);
        }
    }

    private static void gotoFolder(String subFolder) {
        File file = new File(currentPath + "/" + subFolder);

        if (!file.isDirectory()) {
            System.out.println("Error");
        } else {
            currentPath.append("/").append(subFolder);
        }
    }

    public static void copy(String source, String destination) {
        InputStream in;
        OutputStream out;
        try {
            in = new FileInputStream(currentPath.toString() + "/" + source);
            out = new FileOutputStream(currentPath.toString() + "/" + destination);
            out.write(in.readAllBytes());
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
