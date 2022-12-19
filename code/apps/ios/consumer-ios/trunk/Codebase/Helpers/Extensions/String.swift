//
//  String.swift
//  JSBL-BB
//
//  Created by Adnan Ahmed on 19/09/2017.
//  Copyright © 2017 Inov8. All rights reserved.
//

import Foundation

extension String{
    /// String with no spaces or new lines in beginning and end.
    public var trimmed: String {
        return trimmingCharacters(in: CharacterSet.whitespacesAndNewlines)
    }
}
