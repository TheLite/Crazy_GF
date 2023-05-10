var title



// title = document.querySelector("#id") Caso for por ID Usa A tralha antes do nome"#Nome" 
// title = document.querySelector(".classe") Caso for por classe Usa o Ponto antes do nome".Nome"
// title = document.querySelector("tag") Caso for por Tag Usa somente aspas "nome"


title = document.querySelector(".l1") ;// Retorna o primeiro item com essa referência
var input3 = document.querySelectorAll("input") // Retorna uma lista de todas as tags

//colorir automaticamente com qualquer cor de fundo, escolhendo a cor e lista que quer colorir
function colorirBg(lista, quantidade, cor){
    let cont = 0
    while(cont < quantidade){
        lista[cont].style.backgroundColor = cor
        cont ++
    }
}

//Função para mudar a cor quando eu clicar 
function daltonico(){
    let daltonCheck = document.querySelectorAll("input")[4]
    if(!daltonCheck.checked){
        title.style.color = "pink"
    }
    else{
        title.style.color = "blue"
        title.innerHTML = "Bem Vind@ não daltonic@"
    }
}

// Mudar a cor do título para azul
title.style.color = "blue"

// Mudando as cores de fundo manualmente

    // input3[0].style.backgroundColor = "blue"
    // input3[1].style.backgroundColor = "blue"
    // input3[2].style.backgroundColor = "blue"
    // input3[3].style.backgroundColor = "blue"


//Mudando as cores de fundo Automaticamente 
colorirBg(input3, 3, "#666")



// Possíveis impletantações na função colorirBg
// Começa de qual valor da lista e se for fundo ou cor normal

// function colorirBg(lista,comecaDe, quantidade, cor, fundo = true){
//     let cont = comecaDe - 1
//     while(cont < quantidade){
//         if(fundo){
//             lista[cont].style.backgroundColor = cor
//         }
//         else{
//             lista[cont].style.color = cor
//         }        
//         cont ++
//     }
// }

// Como chamar essa função com o mesmo exemplo
// colorirBg(input3, 3, 1, "#666", true)