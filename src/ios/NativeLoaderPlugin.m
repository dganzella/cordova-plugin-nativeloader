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
        loaderView = [[UIImageView alloc] initWithFrame:CGRectMake([[UIScreen mainScreen] bounds].size.width/2-32,[[UIScreen mainScreen] bounds].size.height/2-32,64,64)];
        loaderView.image= [UIImage animatedImageNamed:@"preloader" duration:1.2f];
        loaderView.contentMode = UIViewContentModeCenter;
        loaderView.tag = animViewTag;
        
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
        loaderView.transform = CGAffineTransformScale(CGAffineTransformIdentity, 0, 0);
        
        [self.webView.superview insertSubview:bgView atIndex:50];
        [self.webView.superview insertSubview:loaderView atIndex:51];
        
        [UIView animateWithDuration:0.6
                              delay:0
                            options: UIViewAnimationOptionCurveEaseOut
                         animations:^{
                             loaderView.transform = CGAffineTransformScale(CGAffineTransformIdentity, 1.0, 1.0);
                         }
                         completion:^(BOOL finished){
                         }];
    }
    else
    {
        [UIView animateWithDuration:0.6
                              delay:0
                            options: UIViewAnimationOptionCurveEaseOut
                         animations:^{
                             loaderView.transform = CGAffineTransformScale(CGAffineTransformIdentity, 0.01, 0.01);
                         }
                         completion:^(BOOL finished){
                             [loaderView removeFromSuperview];
                         }];
        
        [UIView animateWithDuration:0.6
                              delay:0.6
                            options: UIViewAnimationOptionCurveEaseIn
                         animations:^{
                             bgView.alpha = 0;
                         }
                         completion:^(BOOL finished){
                             
                             [bgView removeFromSuperview];
                         }];
    }
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
