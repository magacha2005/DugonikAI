import tkinter as tk

root = tk.Tk()
root.title("ДУГОНИК AI ❤️")
root.geometry("360x600")
root.configure(bg="#17172b")

title = tk.Label(
    root,
    text="ДУГОНИК AI ❤️",
    font=("Arial", 24, "bold"),
    fg="white",
    bg="#17172b"
)
title.pack(pady=30)

face = tk.Label(
    root,
    text="😊",
    font=("Arial", 100),
    bg="#17172b",
    fg="white"
)
face.pack(pady=30)

message = tk.Label(
    root,
    text="Салом! Ман дугоники ту ҳастам.",
    font=("Arial", 15),
    fg="white",
    bg="#17172b"
)
message.pack(pady=20)

root.mainloop()
