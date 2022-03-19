FROM 10.133.122.84:80/ci_tools/node:latest

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN echo 'Asia/Shanghai' >/etc/timezone

ENV LANG en_US.utf8

COPY . /opt/

RUN cp -R /opt/dist/* /opt/express/public/

WORKDIR /opt/express/

EXPOSE 80

CMD npm run start