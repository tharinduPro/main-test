@echo off 
set wireless_name="本地连接 2"
set wireless_ipaddress=192.168.3.223
set wireless_mask=255.255.255.0
set wireless_gateway=192.168.3.254
set wireless_metric=10
set wireless_dnsOne=19.104.8.3
set wireless_dnsTwo=19.104.11.3

netsh interface ip set address  source=static name=%wireless_name% addr=%wireless_ipaddress% mask=%wireless_mask% gateway=%wireless_gateway% gwmetric=%wireless_metric%
netsh interface ip set dns name=%wireless_name% static addr=%wireless_dnsOne%
netsh interface ip add dns name=%wireless_name% addr=%wireless_dnsTwo% 2


