public class GenericExe {
    class Gen<T> {
        public T obj;

        public Gen(T obj) {
            super();
            this.obj = obj;
        }

        public T getObj() {
            return obj;
        }

        public void setObj(T obj) {
            this.obj = obj;
        }

        //打印类型
        public void showType() {
            System.out.println(obj.getClass().getName());
        }
    }

    class NoGen {

        private Object obj;

        public NoGen(Object obj) {
            super();
            this.obj = obj;
        }

        public Object getObj() {

            return obj;
        }

        public void setObj(Object obj) {
            this.obj = obj;
        }

        public void showType() {
            System.out.println(obj.getClass().getName());
        }
    }

    class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            super();
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "姓名:\t" + this.getName() + "\t年龄:\t" + this.age;
        }

        public void display() {
            System.out.println("你可以调用这个方法吗?");
        }
    }

    public static void main(String[] args) {

        //使用泛型类

        GenericExe test = new GenericExe();

        Gen<Person> p1 = test.new Gen<>(test.new Person("dada", 23));

        System.out.println("--------------泛型类------------------");

        p1.showType();

        System.out.println("p1 value:\t" + p1.getObj().toString());

        //使用泛型的时候你可以直接的调用这个类里面的任何的方法,因为它的

        //类型是确定的

        p1.getObj().display();

        //不使用泛型

        NoGen p2 = test.new NoGen(test.new Person("dandan", 21));

        System.out.println("--------------非泛型类------------------");

        p2.showType();

        System.out.println("p2 value:\t" + p2.getObj().toString());

        //这个时候你直接去调用除了Object类型里面所定义的方法之外的其他任何的方法都是

        //错误的,因为这个时候你所获得的那个对象是Object类型的,尽管它本质上是可以转换

        //为Person类型的,但是谁知道呢?编译器是不知道的,因此你就需要多一步的转换的过程

        //多这一步的转换过程就有可能会出错,因为你可能有很多的类都要调用这个方法,这很显

        //然是会多出许多的重复的无用的代码的,这里只是写了一个类,如果是成百上千的类,那么

        //就需要多写成百上千的转换,谁能保证不出错?你能?反正我不能.

//        p2.getObj().display();

    }

}

