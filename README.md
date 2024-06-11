# Running the Email Sender Application

This guide provides instructions for setting up and running the Email Sender Application locally using Docker.

## Prerequisites

Before running the application, ensure you have the following prerequisites installed on your machine:

- Docker

## Installation and Setup

1. **Clone the repository to your local machine:**
   ```bash
   git clone https://github.com/olivertate9/email-app.git
   
2. **Change the .env file in root(near pom.xml) with this parameters for sending emails:**
   
   MAIL_USERNAME=yourRealMail@gmail.com
   
   MAIL_PASSWORD=your app token([guide](https://support.google.com/accounts/answer/185833) how to get it)

3. **Run docker-compose to start the app**
   ```bash
   docker-compose up

4. **Send emails**
   
   You can send messages to RabbitMq directly from localhost:15672 or when creating new game using [this](https://github.com/olivertate9/VideoGames) app.
   
