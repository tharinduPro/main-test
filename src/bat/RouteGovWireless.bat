set wireless_name="无线网络连接"
set wireless_ipaddress=192.168.32.4
set wireless_mask=255.255.255.0
set wireless_gateway=192.168.32.2
set wireless_metric=1
set wireless_dnsOne=202.96.128.86
set wireless_dnsTwo=202.96.128.166

netsh interface ip set address  source=static name=%wireless_name% addr=%wireless_ipaddress% mask=%wireless_mask% gateway=%wireless_gateway% gwmetric=%wireless_metric%
netsh interface ip set dns name=%wireless_name% static addr=%wireless_dnsOne%
netsh interface ip add dns name=%wireless_name% addr=%wireless_dnsTwo% 2


