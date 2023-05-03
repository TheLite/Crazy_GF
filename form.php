<?php
    include_once "header/head.php"
?>
<title> Formulário HTML </title>
<body>
    <div class="dad">
        <h1 class="central">
            Formulário de cadastro
        </h1>
        <div class="container">
            <form method="POST" action="">
                <div class="rows">
                    <label for="txUsuario"> Usuário: </label>
                    <input type="text" id="txUsuario" maxlength="16"/>
                </div>
                <div class="rows">
                    <label for="txNome" >Nome: </label>
                    <input class="Brincadeira" type="text" id="txNome" size="50" />
                </div>
                <div class="rows">
                    Data de Nascimento:
                    <input type="date" id="dtNasc" name="aaaaa">
                </div>
                <div class="rows">
                    <label for="txEmail">E-mail: </label>
                    <input type="email" id="txEmail" placeholder = "exemplo@exp.com">
                </div>

                <div class="rows">
                    <label for="txEmail">Número Favorito: </label>
                    <input type="number" id="nmFav" size="5" max="9999" min="0">
                </div>
                <div class="rows">
                    Qual o seu Genero:
                </div>
                <div class="rows">
                <input type="radio" id="Cuie" name="gender"> <label for="Cuie">Mulher</label>
                <input type="radio" id="homo" name="gender"> <label for="homo">Homem</label>
                <input type="radio" id="nop" name="gender"> <label for="nop">Outre</label>
                </div>
                

                <div class="rows">
                    <label for="senhaUsuario">Senha: </label>
                    <input type="password" id="senhaUsuario"  placeholder = "Não digite 1234">
                </div>

                <div class="rows">
                    <input type = "checkbox" id="checka" checked> <label for="checka"> Quero que me mande E-mails promocionais</label>
                </div>
                <div class="rows">
                    <input type="checkbox" id="poschecka" required> <label for="poschecka">Aceito os <a href="#TUSIFUDEU">Termos de uso</a></label>
                </div>
                <input type="submit" value="Cadastrar">
            </form>

        </div>
    </div>
</body>