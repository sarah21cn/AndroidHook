package android.app;

/**
 * Created by yinshan on 2020/8/17.
 *
 * ActivityThread是一个@hide类，无法直接访问
 * 为了避免反射浪费性能，使用ActivityStub包下的ActivityThread来欺骗编译器
 * gradle引入方式：compileOnly
 */
public final class ActivityThread {

  public static ActivityThread currentActivityThread() {
    throw new RuntimeException("Stub!");
  }

}
