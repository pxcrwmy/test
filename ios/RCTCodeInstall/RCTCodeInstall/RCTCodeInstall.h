//
//  RCTCodeInstall.h
//  RCTCodeInstall
//
//  Created by HZDX on 2021/2/19.
//  Copyright © 2021 clb. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CodeInstallSDK.h"

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#elif __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#elif __has_include("React/RCTBridgeModule.h")
#import "React/RCTBridgeModule.h"
#endif

@interface RCTCodeInstall : NSObject<RCTBridgeModule,CodeInstallDelegate>

+ (id<CodeInstallDelegate> _Nonnull)allocWithZone:(NSZone *_Nullable)zone;
@end
