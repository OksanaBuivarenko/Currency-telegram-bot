# Currency-telegram-bot

## Описание
Currency telegram bot - это инструмент для инвестирования. Бот следит за ценой актива и уведомляет пользователя о том, что стоимость достигла желаемого уровня. 
Таким образом, инвестор сможет быстро реагировать на изменения и принимать решения на основе актуальной информации.

## Функционал

Телеграм-бот обладает простым и понятным функционалом:

- принимает и обрабатывает команды от пользователя;
  
- при получении команды /start запускает бота и добавляет данные о пользователе в базу данных.
  
- при получении команды /get_price возвращает пользователю текущее значение стоимости биткоина в долларах США.
  
- при получении команды /get_subscription возвращает пользователю текущую подписку.
  
- при получении команды /subscribe подписывает пользователя на стоимость биткоина.
  
- при получении команды /unsubscribe отменяет подписку пользователя.
  
- автоматически обновляет значение курса биткоина и уведомляет пользователей, подписавшихся на выбранный курс.
  

 ## Технологии
- Java 17
- Spring
- PostgresSQL
- Hibernate
- JPA 
- Telegram bot API 

## Начало работы

1. Установите на свой компьютер [JDK](https://www.oracle.com/cis/java/technologies/downloads/) и среду
разработки [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/download/?section=windows), если они ещё не 
установлены.

2. Загрузите проект-заготовку из Git-репозитория.

3. Запустите PostgresSQL в докере выполнив в терминале команду `docker compose up`.

4. В файле `application.yml` поменяйте Token бота на свой. Вы можете ознакомиться с [инструкцией](https://core.telegram.org/bots/tutorial#obtain-your-bot-token), как создать нового бота и получить токен для использования созданного.

5. Запустите CryptoBotApplication.

Теперь ваш бот доступен в Telegram.

   

