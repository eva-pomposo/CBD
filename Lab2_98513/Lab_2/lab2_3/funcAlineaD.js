funcAlineaC = function () { //funçao devolve os phones cujo seu countryNumber é uma capicua

    var numbers = db.phones.find({}).toArray();

    var capicuas = []
    numbers.forEach(element => {
        var numlst = element.display.split('')
        
        var numReverse = ""
        for (var x = numlst.length - 1; x >= 0; x--){
            numReverse += numlst[x];
        }

        if(element.display.split('-')[1] == numReverse.split('-')[0]){
            capicuas.push(element.display)
        }

    });

    return capicuas
}