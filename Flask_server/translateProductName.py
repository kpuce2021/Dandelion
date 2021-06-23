def translateName(enProductList, transProductList, productName):
    if productName in enProductList:
        return transProductList[enProductList.index(productName)]
    else:
        return "없는 내용 입니다"


productEnName = ["Pepero_Amond", "Pepero_Crunch", "Pepero_Original"]

productKorName = ["빼빼로 아몬드", "빼빼로 크런키", "빼빼로 오리지날"]
