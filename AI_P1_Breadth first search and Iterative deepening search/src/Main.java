import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    // تبدیل آرایه دوبعدی به رشته
    public static String arrayToStr(char[][] arr, int puzzleLength) {
        StringBuilder arrNum = new StringBuilder();
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                arrNum.append(arr[i][j]);

            }
        }
        return arrNum.toString();
    }

    /*
    گسترش وضعیت جاری به تمام حالات ممکن
    * در این تابع با بررسی وجود خانه های 4 طرف ممکن
    * تمام حالات ممکن بعدب را می سازد
    */
    public static void extend(int puzzleLength) {
        char[][] array = new char[puzzleLength][puzzleLength];
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                array[i][j] = nodPros.puzzle.array2D[i][j];
            }
        }
        if (nodPros.puzzle.zeroRow > 0) {
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow - 1][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow - 1][nodPros.puzzle.zeroColumn] = '0';

            nodPros.nextPNum = arrayToStr(array, puzzleLength);
            nodPros.child = new Tree(nodPros.nextPNum, nodPros, puzzleLength);
            nodPros.pointer.add(nodPros.child);

            array[nodPros.puzzle.zeroRow - 1][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = '0';
        }
        if (nodPros.puzzle.zeroRow < puzzleLength - 1) {
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow + 1][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow + 1][nodPros.puzzle.zeroColumn] = '0';

            nodPros.nextPNum = arrayToStr(array, puzzleLength);
            nodPros.child = new Tree(nodPros.nextPNum, nodPros, puzzleLength);
            nodPros.pointer.add(nodPros.child);

            nodPros.puzzle.child.add(arrayToStr(array, puzzleLength));
            array[nodPros.puzzle.zeroRow + 1][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = '0';
        }
        if (nodPros.puzzle.zeroColumn > 0) {
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn - 1];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn - 1] = '0';

            nodPros.nextPNum = arrayToStr(array, puzzleLength);
            nodPros.child = new Tree(nodPros.nextPNum, nodPros, puzzleLength);
            nodPros.pointer.add(nodPros.child);

            nodPros.puzzle.child.add(arrayToStr(array, puzzleLength));
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn - 1] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = '0';
        }
        if (nodPros.puzzle.zeroColumn < puzzleLength - 1) {
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn + 1];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn + 1] = '0';

            nodPros.nextPNum = arrayToStr(array, puzzleLength);
            nodPros.child = new Tree(nodPros.nextPNum, nodPros, puzzleLength);
            nodPros.pointer.add(nodPros.child);

            nodPros.puzzle.child.add(arrayToStr(array, puzzleLength));
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn + 1] = array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn];
            array[nodPros.puzzle.zeroRow][nodPros.puzzle.zeroColumn] = '0';
        }
    }
    // بررسی وجود فرزندان در حالات قبلی برای جلوگیری از ظهور مجدد یک نود در درخت
    public static boolean isExist(String num) {
        for (int i = 0; i < everyNod.size(); i++) {
            if (everyNod.get(i).equals(num))
                return false;
        }
        return true;
    }

    public static ArrayList<String> everyNod = new ArrayList<>();
    // لیستی برای ذخیره تمام حالات به وجود امده (ذخیره تمام نود های درخت)

    //بررسی اینکه ایا به جواب رسیده ایم یا نه
    public static boolean tst(int dimensions) {
        if (dimensions == 3) {
            return nodPros.pNum.equals("012345678");
        } else return (nodPros.pNum.equals("0123456789ABCDEF"));
    }

    static Tree nodPros;//اشاره گری برای پیمایش نود در حال پردازش
    static Tree treePuzzle;//درخت در حال پیمایش

    //تابع اصلی برنامه
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String firstPosition = input.nextLine();//دریافت حالت اولیه از کاربر

        int dimensions = (int) (Math.sqrt(firstPosition.length()));//محاسبه سطر و ستون مورد نیاز برای پازل
        int depth = 0;//عمقی که جواب در ان می باشد
        ArrayList<Tree> path = new ArrayList<>(); // ذخیره مسیر از ریشه تا هدف
        treePuzzle = new Tree(firstPosition, null, dimensions);//ساخت اولین نود درخت (ریشه)
        ArrayList<Tree> frantier = new ArrayList<>();// ذخیره پیشگامانی که استخراج می کنیم ( ذخیره نسل بعدی برای پردازش)
        ArrayList<Tree> oldFrantier = new ArrayList<>();// بررسی پیشگامانی که در دور قبل استخراج کردیم
        frantier.add(treePuzzle);// اضافه کردن ریشه به لیست پیشگامان برای شروع پردازش
        nodPros = treePuzzle;
        everyNod.add(nodPros.pNum);

        /*
        * اجرای الگوریتم اول ( جستجوی اول پهنا) به این صورت انجام گرفته که ابتدا
        * نود در حال پردازش را چک می کند تا اگر به جواب مورد نظر رسیده باشد از حلقه خارج شود
         سپس لیست پیشگامانی که در مرحله قبل بدست اورده را در یک لیست دیگر به نام پیشگامان قبلی انتقال می دهیم
         * تا این لیست را پردازش کنیم و به دنبال جواب باشیم و در صورت نیافتن جواب در این لیست تمام نود ها را بسط بدهیم
        */
        while (!tst(dimensions)) {
            oldFrantier.clear();
            for (int i = 0; i < frantier.size(); i++) {
                oldFrantier.add(frantier.get(i));
            }
            frantier.clear();

            //حلقه برای بسط دادن لیست پیشگامان قبلی
            for (int j = 0; j < oldFrantier.size(); j++) {
                nodPros = oldFrantier.get(j);
                extend(dimensions);
                for (int i = 0; i < nodPros.pointer.size(); i++) {
                    if (isExist(nodPros.pointer.get(i).pNum)) {
                        frantier.add(nodPros.pointer.get(i));
                        everyNod.add(nodPros.pNum);
                    }
                }
            }

            // حلقه برای بررسی تمام نود ها
            for (int i = 0; i < frantier.size(); i++) {
                nodPros = frantier.get(i);
                if (tst(dimensions)) {
                    break;
                }
            }
            depth++;// افزایش شمارنده عمق درخت
        }

        //بعد از یافتن جواب باید مسیر را ذخیره کنیم
        while (nodPros.father != null) {
            path.add(nodPros);
            nodPros = nodPros.father;
        }
        path.add(nodPros);// به علت اضافه نشدن نود ریشه باید ریشه را از اخر به صورت دستی اضافه بکنیم

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("we find answer !");
        for (int i = path.size(); i > 0; i--) {
            nodPros = path.get(i - 1);
            nodPros.puzzle.printArray2D();
            System.out.println("------------");
        }
        depth = path.size();
        System.out.print("depth : ");
        System.out.println(depth - 1);
        System.out.print("every Nods extend : ");
        System.out.println(everyNod.size());

        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
        * اجرای الگوریتم دوم (جستجوی تعمیق تدریجی) به این صورت انجام میگیرد که
        * ابتدا اشاره گر نود در حال پردازش را به ریشه برمی گردانیم و سپس
        *  یک تابع بازگشتی که در پایین تر تعریف شده را صدا می زنیم
        * این تابع اینگونه عمل می کند که هر بار تا عمقی که به ان دسترسی دارد نفوض می کند و پس از نیافتن جواب
        * به برادر سمت راست رفته و ان را بررسی می کند. در صورتی که برادر های راست تمام شوند یک نود به
        * عقب برگشت می کند و برادر راست و فرزندان او را تا عمق مجاز چاپ می کند
        * این روند را انقدر ادامه می دهد تا تمام نود های درخت را تا عمق قابل دسترس بررسی کند
        * و در صورت عدم پیدا کردن جواب از این تابع خارج شده و سپس یک واحد به عمق مجاز اضافه می کند و دوباره ادامه می دهد
        */

        nodPros= path.getLast();//به انتهای لیست که ریشه قرار دارد می رود
        path.clear();//مسیر قبلی پاک می شود
        evNod=1;
        for (int i=2;!tst(dimensions);i++) {
            iterativeDeepening(dimensions, limit);
            limit=i;
        }
        while (nodPros.father != null) {
            path.add(nodPros);
            nodPros = nodPros.father;
        }
        path.add(nodPros);
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("we find answer !");
        for (int i = path.size(); i > 0; i--) {
            nodPros = path.get(i - 1);
            nodPros.puzzle.printArray2D();
            System.out.println("------------");
        }
        depth = path.size();
        System.out.print("depth : ");
        System.out.println(depth - 1);
        System.out.print("every Nods extend : ");
        System.out.println(evNod);
    }
    static int evNod ;//ذخیره تعداد نود های پردازش شده در الگوریتم دوم
    static int limit = 1;

    //تابع بازگشتی برای الگوریتم دوم
    public static void iterativeDeepening(int dimensions , int lim) {
        lim-=1;
        if (lim >= 0) {
            evNod++;
            for (int i = 0; i < nodPros.pointer.size(); i++) {
                nodPros = nodPros.pointer.get(i);
                if (tst(dimensions)) break;
                else {
                    iterativeDeepening(dimensions , lim);
                    nodPros = nodPros.father;
                    lim+=1;
                }
            }
        }
    }
}

//کلاس درخت برای ساخت درخت با تمام ویژگی های مورد نیاز
class Tree {
    final Tree father;
    ArrayList<Tree> pointer;
    Tree child;
    String pNum; //
    String nextPNum;
    Puzzle puzzle;

    Tree(String position, Tree lastNode, int puzzleLength) {
        pNum = position;
        father = lastNode;
        puzzle = new Puzzle(pNum, puzzleLength);
        pointer = new ArrayList<>();

    }
}

// کلاس پازل برای ذخیره و پیمایش هر حالت پازل
class Puzzle {
    public char[][] array2D;//for puzzle shape
    public String puzzleNum;//for puzzle value
    public int zeroRow;//for zero row
    public int zeroColumn;//for zero column
    ArrayList child;//for extending the successors of puzzle
    int puzzleLength;// برای اینکه بفهمیم پازل را باید 3در 3 تولید کنیم یا 4 در 4

    //پیدا کردن مختصات عدد 0 یا در واقع همان خانه خالی
    public void findZero() {
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                if (array2D[i][j] == '0') {
                    zeroRow = i;
                    zeroColumn = j;
                }
            }
        }
    }

    public Puzzle(String number, int puzzleLength) {
        puzzleNum = number;
        this.puzzleLength = puzzleLength;
        child = new ArrayList<>();
        array2D = new char[puzzleLength][puzzleLength];
        int index = 0;
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                array2D[i][j] = number.charAt(index);
                index++;
            }
        }
        findZero();
    }

    //چاپ کردن پازل
    public void printArray2D() {
        for (int i = 0; i < puzzleLength; i++) {
            for (int j = 0; j < puzzleLength; j++) {
                System.out.print(array2D[i][j] + " ");
            }
            System.out.println();
        }
    }
}