# AndroidHook
## 基本原理
- 找到可以hook的点
- 反射修改变量或调用方法
- 动态代理

## 点击事件的Hook
- 动态替换View.getListenerInfo中的mOnClickListener
- 在Activity中递归查找待hook的View进行Hook

## Toast替换Context
- 自定义SafeToastContext，替换View.mContext
- 在getService时替换WindowManager为WindowManagerWrapper
- 在WindowMangerWrapper中重写addView，增加try-catch
