#import "NativeLoaderPlugin.h"

@implementation NativeLoaderPlugin{
	UIView* loaderView;
	bool isViewShown;
}

#pragma mark -
#pragma mark Cordova Methods
-(void) pluginInitialize
{
	self.isViewShown = false;
	
	loaderView = [[UIView alloc] initWithFrame:CGRectMake(0,0,
													 [[UIScreen mainScreen] applicationFrame].size.width,
													 [[UIScreen mainScreen] applicationFrame].size.height)];
													 
	[loaderView setBackgroundColor:[UIColor blackColor]];
}

-(void)showView:(CDVInvokedUrlCommand*)command
{
	bool showView = [[command.arguments objectAtIndex:0] boolValue];
	NSLog(@"show view %@", isPublisher ? @"SHOW" : @"HIDE" );
	
	if(showView ^ self.isViewShown)
	{	
		if(showView)
		{
			[self.webView.superview insertSubview:self.loaderView atIndex:999];
		}
		else
		{
			[self.loaderView removeFromSuperview];
		}	
		
		self.isViewShown = showView;
	}

    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
