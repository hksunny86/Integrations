
#import "SecurityUtility.h"
#import <Security/Security.h>
#import <CommonCrypto/CommonCryptor.h>

static char encodingTable[64] =
{
    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P',
    'Q','R','S','T','U','V','W','X','Y','Z','a','b','c','d','e','f',
    'g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v',
    'w','x','y','z','0','1','2','3','4','5','6','7','8','9','+','/'
};

@interface NSData (AESCrypt)
+ (NSData *)dataWithBase64EncodedString:(NSString *)string;
- (id)initWithBase64EncodedString:(NSString *)string;
- (NSString *)base64Encoding;
- (NSString *)base64EncodingWithLineLength:(NSUInteger)lineLength;
@end

@implementation NSData (AESCrypt)
+ (NSData *)dataWithBase64EncodedString:(NSString *)string
{
    return [[NSData allocWithZone:nil] initWithBase64EncodedString:string];
}

- (id)initWithBase64EncodedString:(NSString *)string
{
    NSMutableData *mutableData = nil;
    
    if( string )
    {
        unsigned long ixtext = 0;
        unsigned long lentext = 0;
        unsigned char ch = 0;
        unsigned char inbuf[4], outbuf[3];
        short i = 0, ixinbuf = 0;
        BOOL flignore = NO;
        BOOL flendtext = NO;
        NSData *base64Data = nil;
        const unsigned char *base64Bytes = nil;
        
        // Convert the string to ASCII data.
        base64Data = [string dataUsingEncoding:NSASCIIStringEncoding];
        base64Bytes = [base64Data bytes];
        mutableData = [NSMutableData dataWithCapacity:base64Data.length];
        lentext = base64Data.length;
        
        while( YES )
        {
            if( ixtext >= lentext ) break;
            ch = base64Bytes[ixtext++];
            flignore = NO;
            
            if( ( ch >= 'A' ) && ( ch <= 'Z' ) ) ch = ch - 'A';
            else if( ( ch >= 'a' ) && ( ch <= 'z' ) ) ch = ch - 'a' + 26;
            else if( ( ch >= '0' ) && ( ch <= '9' ) ) ch = ch - '0' + 52;
            else if( ch == '+' ) ch = 62;
            else if( ch == '=' ) flendtext = YES;
            else if( ch == '/' ) ch = 63;
            else flignore = YES;
            
            if( ! flignore )
            {
                short ctcharsinbuf = 3;
                BOOL flbreak = NO;
                
                if( flendtext )
                {
                    if( ! ixinbuf ) break;
                    if( ( ixinbuf == 1 ) || ( ixinbuf == 2 ) ) ctcharsinbuf = 1;
                    else ctcharsinbuf = 2;
                    ixinbuf = 3;
                    flbreak = YES;
                }
                
                inbuf [ixinbuf++] = ch;
                
                if( ixinbuf == 4 )
                {
                    ixinbuf = 0;
                    outbuf [0] = ( inbuf[0] << 2 ) | ( ( inbuf[1] & 0x30) >> 4 );
                    outbuf [1] = ( ( inbuf[1] & 0x0F ) << 4 ) | ( ( inbuf[2] & 0x3C ) >> 2 );
                    outbuf [2] = ( ( inbuf[2] & 0x03 ) << 6 ) | ( inbuf[3] & 0x3F );
                    
                    for( i = 0; i < ctcharsinbuf; i++ )
                        [mutableData appendBytes:&outbuf[i] length:1];
                }
                
                if( flbreak )  break;
            }
        }
    }
    
    self = [self initWithData:mutableData];
    return self;
}
- (NSString *)base64Encoding
{
    return [self base64EncodingWithLineLength:0];
}

- (NSString *)base64EncodingWithLineLength:(NSUInteger)lineLength
{
    const unsigned char   *bytes = [self bytes];
    NSMutableString *result = [NSMutableString stringWithCapacity:self.length];
    unsigned long ixtext = 0;
    unsigned long lentext = self.length;
    long ctremaining = 0;
    unsigned char inbuf[3], outbuf[4];
    unsigned short i = 0;
    unsigned short charsonline = 0, ctcopy = 0;
    unsigned long ix = 0;
    
    while( YES )
    {
        ctremaining = lentext - ixtext;
        if( ctremaining <= 0 ) break;
        
        for( i = 0; i < 3; i++ )
        {
            ix = ixtext + i;
            if( ix < lentext ) inbuf[i] = bytes[ix];
            else inbuf [i] = 0;
        }
        
        outbuf [0] = (inbuf [0] & 0xFC) >> 2;
        outbuf [1] = ((inbuf [0] & 0x03) << 4) | ((inbuf [1] & 0xF0) >> 4);
        outbuf [2] = ((inbuf [1] & 0x0F) << 2) | ((inbuf [2] & 0xC0) >> 6);
        outbuf [3] = inbuf [2] & 0x3F;
        ctcopy = 4;
        
        switch( ctremaining )
        {
            case 1:
                ctcopy = 2;
                break;
            case 2:
                ctcopy = 3;
                break;
        }
        
        for( i = 0; i < ctcopy; i++ )
            [result appendFormat:@"%c", encodingTable[outbuf[i]]];
        
        for( i = ctcopy; i < 4; i++ )
            [result appendString:@"="];
        
        ixtext += 3;
        charsonline += 4;
        
        if( lineLength > 0 )
        {
            if( charsonline >= lineLength )
            {
                charsonline = 0;
                [result appendString:@"\n"];
            }
        }
    }
    
    return [NSString stringWithString:result];
}
@end

@implementation SecurityUtility

/*
static NSString *base64_encode(NSString *str){
	NSData* data = [str dataUsingEncoding:NSUTF8StringEncoding];
	if(!data){
		return nil;
	}
	return base64_encode_data(data);
}
*/

static NSString *base64_encode_data(NSData *data){
	data = [data base64EncodedDataWithOptions:0];
	NSString *ret = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
	return ret;
}

static NSData *base64_decode(NSString *str){
	NSData *data = [[NSData alloc] initWithBase64EncodedString:str options:NSDataBase64DecodingIgnoreUnknownCharacters];
	return data;
}

+ (NSData *)stripPublicKeyHeader:(NSData *)d_key{
	// Skip ASN.1 public key header
	if (d_key == nil) return(nil);
	
	unsigned long len = [d_key length];
	if (!len) return(nil);
	
	unsigned char *c_key = (unsigned char *)[d_key bytes];
	unsigned int  idx	 = 0;
	
	if (c_key[idx++] != 0x30) return(nil);
	
	if (c_key[idx] > 0x80) idx += c_key[idx] - 0x80 + 1;
	else idx++;
	
	// PKCS #1 rsaEncryption szOID_RSA_RSA
	static unsigned char seqiod[] =
	{ 0x30,   0x0d, 0x06, 0x09, 0x2a, 0x86, 0x48, 0x86, 0xf7, 0x0d, 0x01, 0x01,
		0x01, 0x05, 0x00 };
	if (memcmp(&c_key[idx], seqiod, 15)) return(nil);
	
	idx += 15;
	
	if (c_key[idx++] != 0x03) return(nil);
	
	if (c_key[idx] > 0x80) idx += c_key[idx] - 0x80 + 1;
	else idx++;
	
	if (c_key[idx++] != '\0') return(nil);
	
	// Now make a new NSData from this buffer
	return([NSData dataWithBytes:&c_key[idx] length:len - idx]);
}

//credit: http://hg.mozilla.org/services/fx-home/file/tip/Sources/NetworkAndStorage/CryptoUtils.m#l1036
+ (NSData *)stripPrivateKeyHeader:(NSData *)d_key{
	// Skip ASN.1 private key header
	if (d_key == nil) return(nil);

	unsigned long len = [d_key length];
	if (!len) return(nil);

	unsigned char *c_key = (unsigned char *)[d_key bytes];
	unsigned int  idx	 = 22; //magic byte at offset 22

	if (0x04 != c_key[idx++]) return nil;

	//calculate length of the key
	unsigned int c_len = c_key[idx++];
	int det = c_len & 0x80;
	if (!det) {
		c_len = c_len & 0x7f;
	} else {
		int byteCount = c_len & 0x7f;
		if (byteCount + idx > len) {
			//rsa length field longer than buffer
			return nil;
		}
		unsigned int accum = 0;
		unsigned char *ptr = &c_key[idx];
		idx += byteCount;
		while (byteCount) {
			accum = (accum << 8) + *ptr;
			ptr++;
			byteCount--;
		}
		c_len = accum;
	}

	// Now make a new NSData from this buffer
	return [d_key subdataWithRange:NSMakeRange(idx, c_len)];
}

+ (SecKeyRef)addPublicKey:(NSString *)key{
	NSRange spos = [key rangeOfString:@"-----BEGIN PUBLIC KEY-----"];
	NSRange epos = [key rangeOfString:@"-----END PUBLIC KEY-----"];
	if(spos.location != NSNotFound && epos.location != NSNotFound){
		NSUInteger s = spos.location + spos.length;
		NSUInteger e = epos.location;
		NSRange range = NSMakeRange(s, e-s);
		key = [key substringWithRange:range];
	}
	key = [key stringByReplacingOccurrencesOfString:@"\r" withString:@""];
	key = [key stringByReplacingOccurrencesOfString:@"\n" withString:@""];
	key = [key stringByReplacingOccurrencesOfString:@"\t" withString:@""];
	key = [key stringByReplacingOccurrencesOfString:@" "  withString:@""];
	
	// This will be base64 encoded, decode it.
	NSData *data = base64_decode(key);
	data = [SecurityUtility stripPublicKeyHeader:data];
	if(!data){
		return nil;
	}

	//a tag to read/write keychain storage
	NSString *tag = @"RSAUtil_PubKey";
	NSData *d_tag = [NSData dataWithBytes:[tag UTF8String] length:[tag length]];
	
	// Delete any old lingering key with the same tag
	NSMutableDictionary *publicKey = [[NSMutableDictionary alloc] init];
	[publicKey setObject:(__bridge id) kSecClassKey forKey:(__bridge id)kSecClass];
	[publicKey setObject:(__bridge id) kSecAttrKeyTypeRSA forKey:(__bridge id)kSecAttrKeyType];
	[publicKey setObject:d_tag forKey:(__bridge id)kSecAttrApplicationTag];
	SecItemDelete((__bridge CFDictionaryRef)publicKey);
	
	// Add persistent version of the key to system keychain
	[publicKey setObject:data forKey:(__bridge id)kSecValueData];
	[publicKey setObject:(__bridge id) kSecAttrKeyClassPublic forKey:(__bridge id)
	 kSecAttrKeyClass];
	[publicKey setObject:[NSNumber numberWithBool:YES] forKey:(__bridge id)
	 kSecReturnPersistentRef];
	
	CFTypeRef persistKey = nil;
	OSStatus status = SecItemAdd((__bridge CFDictionaryRef)publicKey, &persistKey);
	if (persistKey != nil){
		CFRelease(persistKey);
	}
	if ((status != noErr) && (status != errSecDuplicateItem)) {
		return nil;
	}

	[publicKey removeObjectForKey:(__bridge id)kSecValueData];
	[publicKey removeObjectForKey:(__bridge id)kSecReturnPersistentRef];
	[publicKey setObject:[NSNumber numberWithBool:YES] forKey:(__bridge id)kSecReturnRef];
	[publicKey setObject:(__bridge id) kSecAttrKeyTypeRSA forKey:(__bridge id)kSecAttrKeyType];
	
	// Now fetch the SecKeyRef version of the key
	SecKeyRef keyRef = nil;
	status = SecItemCopyMatching((__bridge CFDictionaryRef)publicKey, (CFTypeRef *)&keyRef);
	if(status != noErr){
		return nil;
	}
	return keyRef;
}

+ (SecKeyRef)addPrivateKey:(NSString *)key{
	NSRange spos;
	NSRange epos;
	spos = [key rangeOfString:@"-----BEGIN RSA PRIVATE KEY-----"];
	if(spos.length > 0){
		epos = [key rangeOfString:@"-----END RSA PRIVATE KEY-----"];
	}else{
		spos = [key rangeOfString:@"-----BEGIN PRIVATE KEY-----"];
		epos = [key rangeOfString:@"-----END PRIVATE KEY-----"];
	}
	if(spos.location != NSNotFound && epos.location != NSNotFound){
		NSUInteger s = spos.location + spos.length;
		NSUInteger e = epos.location;
		NSRange range = NSMakeRange(s, e-s);
		key = [key substringWithRange:range];
	}
	key = [key stringByReplacingOccurrencesOfString:@"\r" withString:@""];
	key = [key stringByReplacingOccurrencesOfString:@"\n" withString:@""];
	key = [key stringByReplacingOccurrencesOfString:@"\t" withString:@""];
	key = [key stringByReplacingOccurrencesOfString:@" "  withString:@""];

	// This will be base64 encoded, decode it.
	NSData *data = base64_decode(key);
	data = [SecurityUtility stripPrivateKeyHeader:data];
	if(!data){
		return nil;
	}

	//a tag to read/write keychain storage
	NSString *tag = @"RSAUtil_PrivKey";
	NSData *d_tag = [NSData dataWithBytes:[tag UTF8String] length:[tag length]];

	// Delete any old lingering key with the same tag
	NSMutableDictionary *privateKey = [[NSMutableDictionary alloc] init];
	[privateKey setObject:(__bridge id) kSecClassKey forKey:(__bridge id)kSecClass];
	[privateKey setObject:(__bridge id) kSecAttrKeyTypeRSA forKey:(__bridge id)kSecAttrKeyType];
	[privateKey setObject:d_tag forKey:(__bridge id)kSecAttrApplicationTag];
	SecItemDelete((__bridge CFDictionaryRef)privateKey);

	// Add persistent version of the key to system keychain
	[privateKey setObject:data forKey:(__bridge id)kSecValueData];
	[privateKey setObject:(__bridge id) kSecAttrKeyClassPrivate forKey:(__bridge id)
	 kSecAttrKeyClass];
	[privateKey setObject:[NSNumber numberWithBool:YES] forKey:(__bridge id)
	 kSecReturnPersistentRef];

	CFTypeRef persistKey = nil;
	OSStatus status = SecItemAdd((__bridge CFDictionaryRef)privateKey, &persistKey);
	if (persistKey != nil){
		CFRelease(persistKey);
	}
	if ((status != noErr) && (status != errSecDuplicateItem)) {
		return nil;
	}

	[privateKey removeObjectForKey:(__bridge id)kSecValueData];
	[privateKey removeObjectForKey:(__bridge id)kSecReturnPersistentRef];
	[privateKey setObject:[NSNumber numberWithBool:YES] forKey:(__bridge id)kSecReturnRef];
	[privateKey setObject:(__bridge id) kSecAttrKeyTypeRSA forKey:(__bridge id)kSecAttrKeyType];

	// Now fetch the SecKeyRef version of the key
	SecKeyRef keyRef = nil;
	status = SecItemCopyMatching((__bridge CFDictionaryRef)privateKey, (CFTypeRef *)&keyRef);
	if(status != noErr){
		return nil;
	}
	return keyRef;
}

/* START: Encryption & Decryption with RSA private key */

+ (NSData *)encryptData:(NSData *)data withKeyRef:(SecKeyRef) keyRef isSign:(BOOL)isSign {
	const uint8_t *srcbuf = (const uint8_t *)[data bytes];
	size_t srclen = (size_t)data.length;
	
	size_t block_size = SecKeyGetBlockSize(keyRef) * sizeof(uint8_t);
	void *outbuf = malloc(block_size);
	size_t src_block_size = block_size - 11;
	
	NSMutableData *ret = [[NSMutableData alloc] init];
	for(int idx=0; idx<srclen; idx+=src_block_size){
		//NSLog(@"%d/%d block_size: %d", idx, (int)srclen, (int)block_size);
		size_t data_len = srclen - idx;
		if(data_len > src_block_size){
			data_len = src_block_size;
		}
		
		size_t outlen = block_size;
		OSStatus status = noErr;
        
        if (isSign) {
            status = SecKeyRawSign(keyRef,
                                   kSecPaddingPKCS1,
                                   srcbuf + idx,
                                   data_len,
                                   outbuf,
                                   &outlen
                                   );
        } else {
            status = SecKeyEncrypt(keyRef,
                                   kSecPaddingNone,
                                   srcbuf + idx,
                                   data_len,
                                   outbuf,
                                   &outlen
                                   );
        }
		if (status != 0) {
            NSLog(@"SecKeyEncrypt fail. Error Code: %d", (int)status);
			ret = nil;
			break;
		}else{
			[ret appendBytes:outbuf length:outlen];
		}
	}
	
	free(outbuf);
	CFRelease(keyRef);
	return ret;
}

+ (NSString *)encryptString:(NSString *)str privateKey:(NSString *)privKey{
	NSData *data = [SecurityUtility encryptData:[str dataUsingEncoding:NSUTF8StringEncoding] privateKey:privKey];
	NSString *ret = base64_encode_data(data);
	return ret;
}

+ (NSData *)encryptData:(NSData *)data privateKey:(NSString *)privKey{
	if(!data || !privKey){
		return nil;
	}
	SecKeyRef keyRef = [SecurityUtility addPrivateKey:privKey];
	if(!keyRef){
		return nil;
	}
	return [SecurityUtility encryptData:data withKeyRef:keyRef isSign:YES];
}

+ (NSData *)decryptMyData:(NSData *)data withKeyRef:(SecKeyRef) keyRef{
	const uint8_t *srcbuf = (const uint8_t *)[data bytes];
	size_t srclen = (size_t)data.length;
	
	size_t block_size = SecKeyGetBlockSize(keyRef) * sizeof(uint8_t);
	UInt8 *outbuf = malloc(block_size);
	size_t src_block_size = block_size;
	
	NSMutableData *ret = [[NSMutableData alloc] init];
	for(int idx=0; idx<srclen; idx+=src_block_size){
		//NSLog(@"%d/%d block_size: %d", idx, (int)srclen, (int)block_size);
		size_t data_len = srclen - idx;
		if(data_len > src_block_size){
			data_len = src_block_size;
		}
		
		size_t outlen = block_size;
		OSStatus status = noErr;
		status = SecKeyDecrypt(keyRef,
							   kSecPaddingNone,
							   srcbuf + idx,
							   data_len,
							   outbuf,
							   &outlen
							   );
		if (status != 0) {
            NSLog(@"SecKeyEncrypt fail. Error Code: %d", (int)status);
			ret = nil;
			break;
		}else{
			//the actual decrypted data is in the middle, locate it!
			int idxFirstZero = -1;
			int idxNextZero = (int)outlen;
			for ( int i = 0; i < outlen; i++ ) {
				if ( outbuf[i] == 0 ) {
					if ( idxFirstZero < 0 ) {
						idxFirstZero = i;
					} else {
						idxNextZero = i;
						break;
					}
				}
			}
			
			[ret appendBytes:&outbuf[idxFirstZero+1] length:idxNextZero-idxFirstZero-1];
		}
	}
	
	free(outbuf);
	CFRelease(keyRef);
	return ret;
}


+ (NSString *)decryptString:(NSString *)str privateKey:(NSString *)privKey{
	NSData *data = [[NSData alloc] initWithBase64EncodedString:str options:NSDataBase64DecodingIgnoreUnknownCharacters];
	data = [SecurityUtility decryptPrivateData:data privateKey:privKey];
	NSString *ret = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
	return ret;
}

+ (NSData *)decryptPrivateData:(NSData *)data privateKey:(NSString *)privKey{
	if(!data || !privKey){
		return nil;
	}
	SecKeyRef keyRef = [SecurityUtility addPrivateKey:privKey];
	if(!keyRef){
		return nil;
	}
	return [SecurityUtility decryptMyData:data withKeyRef:keyRef];
}

/* END: Encryption & Decryption with RSA private key */

/* START: Encryption & Decryption with RSA public key */

+ (NSString *)encryptString:(NSString *)str publicKey:(NSString *)pubKey{
	NSData *data = [SecurityUtility encryptData:[str dataUsingEncoding:NSUTF8StringEncoding] publicKey:pubKey];
	NSString *ret = base64_encode_data(data);
	return ret;
}

+ (NSData *)encryptData:(NSData *)data publicKey:(NSString *)pubKey{
	if(!data || !pubKey){
		return nil;
	}
	SecKeyRef keyRef = [SecurityUtility addPublicKey:pubKey];
	if(!keyRef){
		return nil;
	}
	return [SecurityUtility encryptData:data withKeyRef:keyRef isSign:NO];
}

+ (NSString *)decryptString:(NSString *)str publicKey:(NSString *)pubKey{
	NSData *data = [[NSData alloc] initWithBase64EncodedString:str options:NSDataBase64DecodingIgnoreUnknownCharacters];
	data = [SecurityUtility decryptMyData:data publicKey:pubKey];
	NSString *ret = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
	return ret;
}

+ (NSData *)decryptMyData:(NSData *)data publicKey:(NSString *)pubKey{
	if(!data || !pubKey){
		return nil;
	}
	SecKeyRef keyRef = [SecurityUtility addPublicKey:pubKey];
	if(!keyRef){
		return nil;
    }
    return [SecurityUtility decryptMyData:data withKeyRef:keyRef];
}
/* END: Encryption & Decryption with RSA public key */
//
+ (NSString *)secureMyString:(NSString *)string andStringKey:(NSString *)key
{
    NSData *plainData = [string dataUsingEncoding:NSUTF8StringEncoding];
    NSData *encryptedData = [SecurityUtility secureMyData:plainData andDataKey:key];
    NSString *encryptedString = [encryptedData base64Encoding];
    return encryptedString;
}
+ (NSData *)secureMyData:(NSData *)data andDataKey:(NSString *)key{
    // 'key' should be 32 bytes for AES256, will be null-padded otherwise
    char keyPtr[kCCKeySizeAES256 + 1]; // room for terminator (unused)
    bzero( keyPtr, sizeof( keyPtr ) ); // fill with zeroes (for padding)
    
    // fetch key data
    [key getCString:keyPtr maxLength:sizeof( keyPtr ) encoding:NSUTF8StringEncoding];
    
    NSUInteger dataLength = [data length];
    
    //See the doc: For block ciphers, the output size will always be less than or
    //equal to the input size plus the size of one block.
    //That's why we need to add the size of one block here
    size_t bufferSize = dataLength + kCCBlockSizeAES128;
    void *buffer = malloc( bufferSize );
    
    size_t numBytesEncrypted = 0;
    CCCryptorStatus cryptStatus = CCCrypt( kCCEncrypt, kCCAlgorithmAES128,
                                          kCCOptionECBMode + kCCOptionPKCS7Padding,
                                          keyPtr, kCCKeySizeAES256,
                                          NULL /* initialization vector (optional) */,
                                          [data bytes], dataLength, /* input */
                                          buffer, bufferSize, /* output */
                                          &numBytesEncrypted );
    if( cryptStatus == kCCSuccess )
    {
        //the returned NSData takes ownership of the buffer and will free it on deallocation
        return [NSData dataWithBytesNoCopy:buffer length:numBytesEncrypted];
    }
    
    free( buffer ); //free the buffer
    return nil;
}

+ (NSString *)unSecureMyString:(NSString *)string withKey:(NSString *)key
{
    NSData *encryptedData = [NSData dataWithBase64EncodedString:string];
    NSData *plainData = [SecurityUtility unSecureMyData:encryptedData withKey:key];
    
    NSString *plainString = [[NSString alloc] initWithData:plainData encoding:NSUTF8StringEncoding];
//
//    NSString *plainString = [[NSString alloc] initWithBytes:[plainData bytes] length:[plainData length] encoding:NSUTF8StringEncoding];

    return plainString;
}

+(NSData*)unSecureMyData:(NSData *)data withKey:(NSString *)key
{
    // 'key' should be 32 bytes for AES256, will be null-padded otherwise
    char keyPtr[kCCKeySizeAES256+1]; // room for terminator (unused)
    bzero( keyPtr, sizeof( keyPtr ) ); // fill with zeroes (for padding)
    
    // fetch key data
    [key getCString:keyPtr maxLength:sizeof( keyPtr ) encoding:NSUTF8StringEncoding];
    
    NSUInteger dataLength = [data length];
    
    //See the doc: For block ciphers, the output size will always be less than or
    //equal to the input size plus the size of one block.
    //That's why we need to add the size of one block here
    size_t bufferSize = dataLength + kCCBlockSizeAES128;
    void *buffer = malloc( bufferSize );
    
    size_t numBytesDecrypted = 0;
    CCCryptorStatus cryptStatus = CCCrypt( kCCDecrypt, kCCAlgorithmAES128, kCCOptionECBMode,
                                          keyPtr, kCCKeySizeAES256,
                                          NULL /* initialization vector (optional) */,
                                          [data bytes], dataLength, /* input */
                                          buffer, bufferSize, /* output */
                                          &numBytesDecrypted );
    
    if( cryptStatus == kCCSuccess )
    {
        //the returned NSData takes ownership of the buffer and will free it on deallocation
        return [NSData dataWithBytesNoCopy:buffer length:numBytesDecrypted];
    }
    
    free( buffer ); //free the buffer
    return nil;
}


@end



