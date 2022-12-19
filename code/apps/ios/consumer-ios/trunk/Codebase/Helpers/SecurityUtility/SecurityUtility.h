#import <Foundation/Foundation.h>

@interface SecurityUtility : NSObject

// return base64 encoded string
+ (NSString *)encryptString:(NSString *)str publicKey:(NSString *)pubKey;
// return raw data
+ (NSData *)encryptData:(NSData *)data publicKey:(NSString *)pubKey;
// return base64 encoded string
+ (NSString *)encryptString:(NSString *)str privateKey:(NSString *)privKey;
// return raw data
+ (NSData *)encryptData:(NSData *)data privateKey:(NSString *)privKey;

// decrypt base64 encoded string, convert result to string(not base64 encoded)
+ (NSString *)decryptString:(NSString *)str publicKey:(NSString *)pubKey;
+ (NSData *)decryptMyData:(NSData *)data publicKey:(NSString *)pubKey;
+ (NSString *)decryptString:(NSString *)str privateKey:(NSString *)privKey;
+ (NSData *)decryptPrivateData:(NSData *)data privateKey:(NSString *)privKey;


+ (NSString *)secureMyString:(NSString *)string andStringKey:(NSString *)key;
+ (NSData *)secureMyData:(NSData *)data andDataKey:(NSString *)key;

+ (NSString *)unSecureMyString:(NSString *)string withKey:(NSString *)key;

+(NSData*)unSecureMyData:(NSData *)data withKey:(NSString *)key;
@end
