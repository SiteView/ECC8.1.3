/* this is a -*-C++-*- file
 * Copyright (C) 2005 Wim Dumon
 *
 * See the LICENSE file for terms of use.
 */

#include <WStringUtil>

#include "Dictionary.h"
#include <fstream>
#include <iostream>
#include <time.h>
#include <stdlib.h>

std::wstring RandomWord(Dictionary dictionary)
{
   std::ifstream dict;
   if(dictionary == DICT_NL) {
      dict.open("dict-nl.txt");
   } else { // english is default
      dict.open("dict.txt");
   }
      
   std::string retval;
   int numwords = 0;
   while(dict) {
      getline(dict, retval);
      numwords++;
   }
   dict.clear();
   dict.seekg(0);

   srand(time(0));
   int selection = rand() % numwords; // not entirely uniform, but who cares?

   while(selection--) {
      getline(dict, retval);
   }
   getline(dict, retval);
   for(unsigned int i = 0; i < retval.size(); ++i)
      if(retval[i] < 'A' || retval[i] > 'Z')
	 std::cout << "word " << retval << " contains illegal data at pos " << i << std::endl;

   return Wt::widen(retval);
}

