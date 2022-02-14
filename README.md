# BFCM

This project is about an e-commerce high-performance system. Basically, people can walk through the products on the website and order online. I implemented a UserLogin page and 
stored user sessions in Redis to handle distributed session storage. I also built product list/product detail/order detail pages using Thymeleaf to do asynchronous page rendering.
An interesting feature is that I designed a count-down controller so people may not be able to buy the product until the count-down is over. This is similar to Black Friday 
activities.
