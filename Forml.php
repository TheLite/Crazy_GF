<!DOCTYPE html5>
<html lang="pt-br">

<head>
   <meta charset="UTF-8">
   <link rel="stylesheet" href="SRC/CSS/stylef.css">
   <title>Formulário</title>
</head>
    <body>
        <div class="papis">
            <h1 class="l1">Petição para acabar com os daltônicos</h1>
            <form method="POST">
                <div class="Guanabara">
                    <label for="txEmail" id="txEmail">E-mail</label>
                    <input type="email" id="txEmail"/>                    
                    <label for="txNick">Nickname:</label>
                    <input type="text" id="txNick" maxlength="18"/>
                    <br>
                    <label for="txPass">Senha:</label>
                    <input type="password" for="txPass"/>
                    <label for="dtNc" id="DataN">Data de Nascimento</label>
                    <input type="date" for="dtNc"/>
                    <br>
                    <label for="checagemdfudido" id="sou fudido">Não sou daltônico</label>
                    <input type="checkbox" id="sou fudido"/>
                    <br>
                    <textarea cols="30" rows="5"></textarea>
                    <input type="range" value="ARREGAÇA"/>
                    
                    <!-- Botão para mudar de cor Usando a função criada em script.js-->
                    <input type = "button" value="Tritanomalia" onclick="daltonico()">

                    <input type="submit" value="Entregar">
                </div>
            </form> 
        </div>    
        <script src="SRC/js/script.js"></script>
    </body>




</html>  