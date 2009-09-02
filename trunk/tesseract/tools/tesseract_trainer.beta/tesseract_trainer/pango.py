#-*- coding:utf8 -*-
#!/usr/local/bin/python

import cairo
import pango
import pangocairo

surface = cairo.ImageSurface(cairo.FORMAT_ARGB32, 600, 100)
context = cairo.Context(surface)

pc = pangocairo.CairoContext(context)

layout = pc.create_layout()
layout.set_font_description(pango.FontDescription("Lohit Bengali Bold 14"))
layout.set_text("অন্ধকারে চৌরাশীটা নরকের কুন্ড, তাহাতে ডুবায়ে ধরে পাতকীর মুন্ড")

# Next four lines take care of centering the text. Feel free to ignore ;-)
width, height = surface.get_width(), surface.get_height()
w, h = layout.get_pixel_size()
position = (width/2.0 - w/2.0, height/2.0 - h/2.0)
context.move_to(*position)

pc.show_layout(layout)

surface.write_to_png("banglatext.png")