<h1>Mafia-online</h1>
<p>
<a>
  <img src="https://camo.githubusercontent.com/417e7b5af4112c19772ec9e986c59e6076e4ad81/68747470733a2f2f63646e2e69636f6e73636f75742e636f6d2f69636f6e2f667265652f706e672d3235362f677261646c652d332d313137353032362e706e67">
</a>
<a>
  <img src="https://camo.githubusercontent.com/5c58a7bdd84854e504466628e57ff0ac3cd10d6e/68747470733a2f2f7777772e6d6174742d74686f726e746f6e2e6e65742f776f726470726573732f77702d636f6e74656e742f75706c6f6164732f30646437313933662d653734372d346131352d623739372d3831386239666163333635362d6d7973716c2e706e67">
</a> 
 <a>
  <img src="https://camo.githubusercontent.com/e8293376c6ea1d2181eb2fa6f878acd806cf0114/68747470733a2f2f64317136663061656c7830706f722e636c6f756466726f6e742e6e65742f70726f647563742d6c6f676f732f36343464326631352d633564622d343733312d613335332d6163653632333538343166612d72656769737472792e706e67">
</a>
  </p>
<p>
<a>
  <img src="https://www.websparrow.org/wp-content/uploads/2019/08/spring.png">
</a>
</p>

<h1>Данный проект является web-приложением. Он представляет из себя игру "Мафия", расчитанную на 5-10 игроков в одной комнате. Проект создан с использованием технологий: Spring Framework, Gradle, MySQL, Docker.<h1>
<h2>Сборка программы</h2>
  <pre><code>$ gradle build</code></pre>
<h2>Создание .jar файла</h2>
  <pre><code>$ gradle bootJar</code></pre>
<h2>Запуск программы</h2>
  <pre><code>$ gradle bootRun</code></pre>
<h2>Запуск программы из Docker-а</h2>
  <pre><code>$ docker build -t mafia .
$ docker run -p PORT:PORT mafia </code></pre>
<h2>Удалить все образы можно с помощью:</h2>
  <pre><code>$ chmod +x dockerRm.sh
$ sh dockerRm.sh</code></pre>
<h2>Команда проекта:</h2>
  <ul>
    <li> Evgenii Osipov | https://github.com/osipovgit</li>
    <li> Maxim Dovydenko | https://github.com/MaksimDov</li>
