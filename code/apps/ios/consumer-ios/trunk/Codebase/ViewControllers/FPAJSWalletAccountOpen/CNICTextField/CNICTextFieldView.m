//
//  CNICTextFieldView.m
//  CNICTextFieldView
//
//  Created by Test on 10/04/2017.
//  Copyright © 2017 <#Company Name#>. All rights reserved.
//

#import "CNICTextFieldView.h"

NSInteger const kFirstPhaseLength = 5;
NSInteger const kSecondPhaseLength = 7;
NSInteger const kThirdPhaseLength = 1;


@interface CNICTextFieldView () <UITextFieldDelegate>
{
    __weak IBOutlet UITextField *cnicTextFirst;
    __weak IBOutlet UITextField *cnicTextSecond;
    __weak IBOutlet UITextField *cnicTextThird;
}
@end

@implementation CNICTextFieldView

- (instancetype)initWithCoder:(NSCoder *)coder
{
    self = [super initWithCoder:coder];
    if (self) {
        
    }
    return self;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    cnicTextFirst.delegate = self;
    cnicTextSecond.delegate = self;
    cnicTextThird.delegate = self;
    [cnicTextThird setKeyboardType:UIKeyboardTypePhonePad];
    [cnicTextSecond setKeyboardType:UIKeyboardTypePhonePad];
    [cnicTextFirst setKeyboardType:UIKeyboardTypePhonePad];
    
//    [Utility addPaddingToTextfield:cnicTextThird withLeftWidth:8 andRightWidth:0];
//    [Utility addPaddingToTextfield:cnicTextSecond withLeftWidth:8 andRightWidth:0];
//    [Utility addPaddingToTextfield:cnicTextFirst withLeftWidth:8 andRightWidth:0];
//    
//    [cnicTextFirst addTarget:self action:@selector(textFieldTextChanged:) forControlEvents:UIControlEventEditingChanged];
//    [cnicTextSecond addTarget:self action:@selector(textFieldTextChanged:) forControlEvents:UIControlEventEditingChanged];
//    [cnicTextThird addTarget:self action:@selector(textFieldTextChanged:) forControlEvents:UIControlEventEditingChanged];
    
//    IQKeyboardManager.shared().keyboardDistanceFromTextField = 80
    //[IQKeyboardManager sharedManager].keyboardDistanceFromTextField = 30;
    
    
}

-(void) setCustomerCNIC: (NSString *)cnic{
    _cnicString = cnic;
    if (_cnicString.length!=0)
    {
        if (_cnicString.length == (kFirstPhaseLength+kSecondPhaseLength+kThirdPhaseLength))
        {
            cnicTextFirst.text = [_cnicString substringToIndex:kFirstPhaseLength];
            NSString *cnicLast = [_cnicString substringFromIndex:kFirstPhaseLength];
            cnicTextSecond.text = [cnicLast substringToIndex:kSecondPhaseLength];
            cnicTextThird.text = [cnicLast substringFromIndex:kSecondPhaseLength];
            
            [self setUserInteractionEnabled:NO];
            
//            UIColor *disabledColor = [Utility disabledColor];
//            
//            cnicTextFirst.backgroundColor = disabledColor;
//            cnicTextSecond.backgroundColor = disabledColor;
//            cnicTextThird.backgroundColor = disabledColor;
        }
    }
    
}

- (NSString *)getCNIC {
//    if (![self isValidCNIC]) {
//        return @"";
//    }
    
    return [NSString stringWithFormat:@"%@%@%@", cnicTextFirst.text ,cnicTextSecond.text ,cnicTextThird.text];
}

-(BOOL)isEmpty{
    return (cnicTextFirst.text.length == 0 || cnicTextSecond.text.length == 0 || cnicTextThird.text.length == 0);
}

- (BOOL)isValidCNIC {
    BOOL isValid = (cnicTextFirst.text.length == kFirstPhaseLength && cnicTextSecond.text.length == kSecondPhaseLength && cnicTextThird.text.length == kThirdPhaseLength);
    //isValid = (cnicTextFirst.text.length == 0 || cnicTextSecond.text.length == 0 || cnicTextThird.text.length == 0)?false:true;
    return isValid;
}
#pragma mark -
#pragma mark UITextFieldDelegate
#pragma mark -

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string
{
    NSCharacterSet *unacceptedInput = nil;
    unacceptedInput = [NSCharacterSet characterSetWithCharactersInString:@"0123456789"];
    
    if ([[string componentsSeparatedByCharactersInSet:unacceptedInput] count] <= 1 && ![string isEqualToString:@""])
    {
        return NO;
    }
    
    if ([cnicTextFirst isEqual:textField])
    {
        NSString *resultText = [textField.text stringByReplacingCharactersInRange:range
                                                                       withString:string];
        if (resultText.length > kFirstPhaseLength && ![string isEqual:@""])
        {
            
            if (cnicTextSecond.text.length < kSecondPhaseLength) {
                [cnicTextSecond becomeFirstResponder];
            }
            else if (cnicTextThird.text.length < kThirdPhaseLength)
            {
                [cnicTextThird becomeFirstResponder];
            }
            else
            {
                [textField resignFirstResponder];
                return NO;
            }
            
        }
    }
    else if ([cnicTextSecond isEqual:textField])
    {
        NSString *resultText = [textField.text stringByReplacingCharactersInRange:range
                                                                       withString:string];
        if (cnicTextSecond.text.length == 1 && [string isEqual:@""])
        {
            cnicTextSecond.text = @"";
            [cnicTextFirst becomeFirstResponder];
            return NO;
        }
        else if (resultText.length > kSecondPhaseLength && ![string isEqual:@""])
        {
            
            if (cnicTextThird.text.length < kThirdPhaseLength)
            {
                [cnicTextThird becomeFirstResponder];
            }
            else{
                [textField resignFirstResponder];
                return NO;
            }
        }
    }
    else if ([cnicTextThird isEqual:textField])
    {
        NSString *resultText = [textField.text stringByReplacingCharactersInRange:range
                                                                       withString:string];
        if (cnicTextThird.text.length == 1 && [string isEqual:@""])
        {
            cnicTextThird.text = @"";
            if (cnicTextSecond.text.length == 0)
            {
                [cnicTextFirst becomeFirstResponder];
            }
            else
            {
                [cnicTextSecond becomeFirstResponder];
            }
            return NO;
        }
        else if(resultText.length >= kThirdPhaseLength+1 && ![string isEqual:@""])
        {
            [textField resignFirstResponder];
            return NO;
        }
    }
    return YES;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    return YES;
}

- (void)textFieldDidEndEditing:(UITextField *)textField {
    if (self.delegate && [self.delegate respondsToSelector:@selector(didEndEditing:)]) {
        [self.delegate didEndEditing:self];
    }
}

- (void)textFieldDidBeginEditing:(UITextField *)textField {
    if (self.delegate && [self.delegate respondsToSelector:@selector(didBeginEditing:)]) {
        [self.delegate didBeginEditing:self];
    }
}

@end
