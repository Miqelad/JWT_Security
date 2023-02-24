<h1> Данный проект является реализацией работы JWT + Spring Security.</h1>
<br>Приложение использует только REST, фронт отсутствует. Для работоспособности проекта, необходимо настроить базу данных, в проекте используется PostgreSQL:
настройки располагаются src/main/resources/application.yaml, при использование данных настроек необходимо
изменить путь к бд, логин и пароль. Так же сгенерировать пароль(необязательно), в properties jwt-secret, пароль
необходимо подставить 256+.</br>
<br><a href="https://jwt.io/">Дополнительная информация и проверка токена.</a>
<br><br>
<ul><strong>Stack:</strong>: 
  <li>Spring boot 3.0.2</li>
  <li>Spring web</li>
  <li>Spring Security</li>
  <li>Spring DATA JPA</li>
  <li>PostgreSQL</li>
  <li>java-jwt 3.18.2</li>
</ul>



<ul><strong>Endpoints</strong>:
<li>http://localhost:8080/api/auth/register</li> - регистрация пользователя
<li>http://localhost:8080/api/auth/login</li> - логин, так же обновление токена
<li>http://localhost:8080/api/user/info</li> - информация о пользователе, необходима только авторизация
</ul>

