FROM python:3.8.2-alpine
WORKDIR /frontend
COPY . /frontend
ENV SERVER_PORT 80
EXPOSE 80
CMD [ "python3", "-m", "http.server", "80"]