#import "NativeLoaderPlugin.h"

@implementation NativeLoaderPlugin{
	UIView* loaderView;
	bool isViewShown;
}

#pragma mark -
#pragma mark Cordova Methods
-(void) pluginInitialize
{
	int viewTag = 7876;
	
	loaderView = [self.webView.superview viewWithTag: viewTag];
	
	if(loaderView == nil)
	{
		loaderView = [[UIView alloc] initWithFrame:CGRectMake(0,0,
													 [[UIScreen mainScreen] applicationFrame].size.width,
													 [[UIScreen mainScreen] applicationFrame].size.height)];
													 
		[loaderView setBackgroundColor:[UIColor blackColor]];
		loaderView.tag = viewTag;	
	}
}

-(void)showView:(CDVInvokedUrlCommand*)command
{
	bool showView = [[command.arguments objectAtIndex:0] boolValue];
	NSLog(@"show view %@", isPublisher ? @"SHOW" : @"HIDE" );
	
	if(showView)
	{
		[self.webView.superview insertSubview:self.loaderView atIndex:999];
	}
	else
	{
		[self.loaderView removeFromSuperview];
	}	
	
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
