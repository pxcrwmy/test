//
//  RCTCodeInstall.m
//  RCTCodeInstall
//
//  Created by HZDX on 2021/2/19.
//  Copyright © 2021 clb. All rights reserved.
//

#import "RCTCodeInstall.h"

#if __has_include(<React/RCTBridge.h>)
#import <React/RCTEventDispatcher.h>
#import <React/RCTRootView.h>
#import <React/RCTBridge.h>
#import <React/RCTLog.h>

#elif __has_include("RCTBridge.h")
#import "RCTEventDispatcher.h"
#import "RCTRootView.h"
#import "RCTBridge.h"
#import "RCTLog.h"

#elif __has_include("React/RCTBridge.h")
#import "React/RCTEventDispatcher.h"
#import "React/RCTRootView.h"
#import "React/RCTBridge.h"
#import "React/RCTLog.h"
#endif

#define CodeinstallWakeupCallBack @"CodeinstallWakeupCallBack"

@interface RCTCodeInstall ()
@property (nonatomic, strong)NSDictionary *wakeUpParams;
@property (nonatomic, assign)BOOL notFirstLoad;
@end

@implementation RCTCodeInstall

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE(CodeinstallModule);

+ (id)allocWithZone:(NSZone *)zone {
    static RCTCodeInstall *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [super allocWithZone:zone];
        sharedInstance.wakeUpParams = [[NSDictionary alloc] init];
    });
    return sharedInstance;
}

RCT_EXPORT_METHOD(getInstall:(RCTResponseSenderBlock)callback)
{
    [CodeInstallSDK  getInstallParams:^(CodeInstallData * _Nullable appData) {

        if (!appData.data&&!appData.channelNo) {
            NSArray *params = @[[NSNull null]];
            callback(params);
            return;
        }
        NSDictionary *dic = @{@"channel":appData.channelNo?:@"",@"data":appData.data?:@""};
        NSArray *params = @[dic];
        callback(params);
    }];
}

- (void)getWakeUpParams:(CodeInstallData *)appData{
    if (!appData.data&&!appData.channelNo) {
        if (self.bridge) {
            [self.bridge.eventDispatcher sendAppEventWithName:CodeinstallWakeupCallBack body:nil];
        }
        return;
    }
    NSDictionary *params = @{@"channel":appData.channelNo?:@"",@"data":appData.data?:@""};
    if (self.bridge) {
        [self.bridge.eventDispatcher sendAppEventWithName:CodeinstallWakeupCallBack body:params];
    }else{
        @synchronized(self){
            self.wakeUpParams = params;
        }
    }
}

RCT_EXPORT_METHOD(getWakeUp:(RCTResponseSenderBlock)callback)
{
    if (!self.notFirstLoad) {
        if (self.wakeUpParams.count != 0) {
            NSArray *params = @[self.wakeUpParams];
            callback(params);
        }else{
            callback(@[[NSNull null]]);
        }
        self.notFirstLoad = YES;
    }
}

RCT_EXPORT_METHOD(reportRegister)
{
    [CodeInstallSDK reportRegister];
}

@end
