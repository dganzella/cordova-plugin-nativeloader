#import "NativeLoaderPlugin.h"

@implementation NativeLoaderPlugin{
    UIView* bgView;
    UIImageView * loaderView;
    bool isViewShown;
}

#pragma mark -
#pragma mark Cordova Methods
-(void) pluginInitialize
{
    
    int animViewTag = 7876;
    int bgViewTag = 7877;
    
    loaderView = (UIImageView*)[self.webView.superview viewWithTag: animViewTag];
    bgView = [self.webView.superview viewWithTag: bgViewTag];
    
    if(loaderView == nil)
    {
        loaderView = [[UIImageView alloc] initWithFrame:CGRectMake(0,0,64,64)];
        loaderView.image= [UIImage animatedImageNamed:@"preloader" duration:1.2f];
        loaderView.contentMode = UIViewContentModeCenter;
        
        bgView = [[UIView alloc] initWithFrame:CGRectMake(0,0,
                                                          [[UIScreen mainScreen] bounds].size.width,
                                                          [[UIScreen mainScreen] bounds].size.height)];
        
        [bgView setBackgroundColor:[UIColor whiteColor]];
        bgView.tag = bgViewTag;
    }
}

-(void)showView:(CDVInvokedUrlCommand*)command
{
    bool showView = [[command.arguments objectAtIndex:0] boolValue];
    NSLog(@"show view %@", showView ? @"SHOW" : @"HIDE" );
    
    if(showView)
    {
        [self.webView.superview insertSubview:bgView atIndex:50];
        [self.webView.superview insertSubview:loaderView atIndex:51];
    }
    else
    {
        [loaderView removeFromSuperview];
        [bgView removeFromSuperview];
    }
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
