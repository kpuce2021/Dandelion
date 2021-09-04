def translateName(enProductList, transProductList, productName):
    if productName in enProductList:
        return transProductList[enProductList.index(productName)]
    else:
        return "없는 내용 입니다"


productEnName = ['Pepero_Original', 'Pepero_Crunch', 'Pepero_Amond', 'Jollypong', 'Chocosongyi',
                 'Churros', 'Kancho', 'Corncho', 'DemisodaPeach', 'DemisodaApple', 'DemisodaGrape']

productKorName = ["빼빼로 아몬드", "빼빼로 크런키", "빼빼로 오리지날", '죠리퐁',
                  '초코송이', '츄러스', '칸쵸', '콘쵸', '데미소다 복숭아', '데미소다 사과', '데미소다 포도']
