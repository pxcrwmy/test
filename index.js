import {
    NativeModules,
    Platform,
    DeviceEventEmitter,
} from 'react-native';

const CodeinstallModule = NativeModules.CodeinstallModule;

const receiveWakeUpEvent = 'CodeinstallWakeupCallBack'
const listeners = {}

export default class codeinstall{

/**
 * 获取安装动态参数
 * @param {Function} cb = (result）=> {} data和channel都为空时返回null
 */
  static getInstall(cb){
  CodeinstallModule.getInstall(result => {
        cb(result)
      }
    )
  }
 
/**
 * 监听univeral link或scheme拉起参数回调的方法
 * @param {Function} cb = (result）=> {} data和channel都为空时返回null
 */
  static addWakeUpListener (cb) {
  CodeinstallModule.getWakeUp(
      result => {
        cb(result)
      }
    )
	listeners[cb] = DeviceEventEmitter.addListener(
		receiveWakeUpEvent,
		result => {
			cb(result)
		}
	)
    
  }

 /**
  * 取消监听
  * @param {Function} cb = (Object) => { }
  */
  static removeWakeUpListener (cb) {
    if (!listeners[cb]) {
      return
    }
    listeners[cb].remove()
    listeners[cb] = null
  }

/**
 * 上报注册事件
 */
  static reportRegister (){
    CodeinstallModule.reportRegister()
  }
}

