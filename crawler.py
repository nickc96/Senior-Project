from bs4 import BeautifulSoup
import urllib
from urllib import request
import nltk, re, pprint
from nltk import word_tokenize
import string
import sys
import time

#url = "https://myanimelist.net/anime/19429/Akuma_no_Riddle"
#html = request.urlopen(url).read().decode('utf8')
#html[:60]
#if "Looking for information on the anime" in html:
#    print("anime")
#print(html)

#url = "https://myanimelist.net/manga/25/Fullmetal_Alchemist"
#html = request.urlopen(url).read().decode('utf8')
#html[:60]
#if "Looking for information on the anime" in html:
#    print("anime")
#else:
#    print("not anime")

#unicode = ''
#for i in range(sys.maxunicode):
#    unicode = unicode + chr(i)
#printable = set(unicode) #printable = set(string.printable)  ∀★
count = 9563
num = 0             # Total Anime: 7214
pages = []
while count < 9564:
    url = "https://myanimelist.net/anime/" + str(count)
    try:
        html = request.urlopen(url).read().decode('ISO-8859-1') #ISO-8859-1
        html[:60]
        pages.append(html)
        num += 1
    except urllib.error.HTTPError as err:
        print(err.code)
    #html = request.urlopen(url).read().decode('utf8')
    
    time.sleep(2)
    count += 1
    print(count)
print("Number of anime:")
print(num)
numAnime = num
print('\n')
count = 0
text = []
while count < num:
    
    #print(pages[0])
    raw = BeautifulSoup(pages[count], "html.parser").get_text()
    #print(raw)
    text.append(raw)
    #tokens = word_tokenize(raw)
    #print(tokens)
    count += 1

#print(text[0])
#text[0].concordance('genres')

with open('Anime list1.txt', 'w', encoding='ISO-8859-1') as text_file:
    #text_file = open("Anime list.txt", "w")
    j=0
    while j < len(text):
        i=0
        title = ''
        tokens = word_tokenize(text[j]) #get title
        text[j] = nltk.Text(tokens)
        while tokens[i] != '-':
            title += tokens[i]
            title += ' '
            i += 1
        title = title[:-1]

        genres = ''
        i = tokens.index('genres') + 1 #get genres
        while tokens[i] != ']':
            genres += tokens[i]
            genres += ' '
            i += 1
        genres = genres.replace(',', '')
        genres = genres.replace('[', '')
        genres = genres.replace('`', '')
        genres = genres.replace('"', '')
        genres = genres.replace('\'', '')
        genres = genres.replace('    ', ' ')
        genres = genres[1:]
        genres = genres[:-1]
        
        studios = ''
        i = tokens.index('Studios') + 2 #get studios
        while tokens[i] != ':' and tokens[i] != 'Source':
            studios += tokens[i]
            studios += ' '
            #print(tokens[i])
            i += 1
        studios = studios[:-1]

        year = ''
        i = tokens.index('Aired', tokens.index('Aired') + 1) + 5 #get year
        year = tokens[i]

        score = ''
        i = tokens.index('Statistics') + 3 #get score
        score = tokens[i]

        typeShow = ''
        i = tokens.index('Type') + 2 #get type (TV, movie, OVA, etc.)
        typeShow = tokens[i]
        
        
        #print(title)
        #print(genres)
        #print(studios)
        #print(year)
        #print(score)
        #print(typeShow)
        #print('\n')

        genres = genres.replace(' ', ',') # fix up genres a bit
        genres = genres.replace('Slice,', 'Slice ')
        genres = genres.replace('of,', 'of ')
        genres = genres[:-1]

        #title = ''.join(filter(lambda x: x in printable, title)) # remove unprintable characters
            #''.join(filter(lambda x: x in printable, title))
        anime = title + '~' + genres + '~' + studios + '~' + year + '~' + score + '~' + typeShow + '\n'#make line to output
        print(anime)
        print(j)
        print('\n')
        #anime = anime.encode('ISO-8859-1')
        text_file.write(anime)#.encode('ISO-8859-1')
        j += 1
text_file.close()
print("Number of anime:")
print(numAnime)

#text[0].concordance('Aired')
#print(tokens.index('genres'))
#32175 anime
