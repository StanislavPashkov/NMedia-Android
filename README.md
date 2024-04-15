[![CI](https://github.com/StanislavPashkov/NMedia-Android/actions/workflows/blank.yml/badge.svg)](https://github.com/StanislavPashkov/NMedia-Android/actions/workflows/blank.yml)
1. Какой из обработчиков сработал при клике на кнопку Like?
2. Сработал ли обработчик на binding.root при клике на кнопку с тремя точками?
3. Сработал ли обработчик на binding.root при клике на текст?
4. Сработал ли обработчик на binding.root при клике на аватар до установки на avatar собственного обработчика?
5. Сработал ли обработчик на binding.root при клике на аватар после установки на avatar собственного обработчика?

1. 2024-03-24 21:46:09.207  4676-4676  MyLog  D  clickLike
2. Не сработал
3. Не сработал на тексте main_text
4. 2024-03-24 21:51:54.061  4347-4347  MyLog  D  clickRoot
5. Не сработал

* clickRoot сработал на тексте author
* clickRoot сработал на тексте published
* clickRoot не сработал на тексте main_text
* clickRoot сработал при нажатии в пустом месте

 Немного непонятно как он работает. Ясно что он не срабатывает на кнопках если они не назначены. 
 Про текст для меня пока загадка, непонятно почему на тексте возле аватара сработал, а внутри барьеров нет.
